package az.expressbank.book.service.impl;

import az.expressbank.book.client.CategoryClient;
import az.expressbank.book.data.dto.BookDTO;
import az.expressbank.book.data.dto.CategoryDTO;
import az.expressbank.book.data.entity.Book;
import az.expressbank.book.data.repository.BookRepository;
import az.expressbank.book.exception.DTOParsingException;
import az.expressbank.book.exception.NotFoundException;
import az.expressbank.book.mapper.BookMapper;
import az.expressbank.book.service.BookService;
import az.expressbank.book.util.BookConverter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import feign.Response;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookServiceImpl implements BookService {
    private final BookMapper bookMapper;

    private final CategoryClient categoryClient;
    private final BookRepository bookRepository;

    public List<BookDTO> getAll() throws Exception {
        log.info("Fetching all books");

        List<Book> books = bookRepository.findAll();
        List<Long> ids = books.stream().map(i -> i.getId()).collect(Collectors.toList());

        feign.Response response;
        try {
            response = categoryClient.getCategoryByIds(ids);
        } catch (Exception ex) {
            log.debug(ex.getMessage());
            throw new Exception();
        }


        List<CategoryDTO> categoryDTOS= BookConverter.getCategoryListFromResponse(response);

        List<BookDTO> dtos = new ArrayList<>();
        for (int i = 0; i < books.size(); i++) {
            BookDTO dto = bookMapper.entityToDTO(books.get(i));
            dto.setCategoryDTO(categoryDTOS.get(i));
            dtos.add(dto);
        }
        log.info("Successfully fetched all books");
        log.trace("Books: "+dtos);
        return dtos;
    }

    public BookDTO getById(Long id) throws Exception {
        log.info("Fetching book by ID: {}", id);

        Book book;
        BookDTO dto;
        try {
            book = bookRepository.getReferenceById(id);
            dto = bookMapper.entityToDTO(book);
        } catch (EntityNotFoundException ex) {
            log.error("Entity not found with id {}", id+"  "+ ex);
            throw new NotFoundException("Entity not found with id " + id);
        }

        feign.Response response;
        try {
            response = categoryClient.getCategoryById(id);
        } catch (Exception ex) {
            log.debug(ex.getMessage());
            throw new Exception();
        }

        CategoryDTO categoryDTO=BookConverter.getCategoryFromResponse(response);
        dto.setCategoryDTO(categoryDTO);
        log.info("Successfully fetched book by ID: {}", id);
        log.trace("CategoryDTO: "+categoryDTO);

        return dto;
    }

    public Long addBook(BookDTO bookDTO) throws Exception {
        log.info("Adding a new book");

        CategoryDTO categoryDTO = bookDTO.getCategoryDTO();
        feign.Response response;
        try {
            response = categoryClient.addCategory(categoryDTO);
        } catch (Exception ex) {
            log.debug(ex.getMessage());
            throw new Exception();
        }

        Long id=BookConverter.getIdOfCategory(response);
        Book book = bookMapper.DTOToEntity(bookDTO);
        System.out.println(bookDTO.getName());

        book.setCategoryId(id);
        Long bookId = bookRepository.saveAndFlush(book).getId();
        log.info("Successfully added a new book with ID {}", bookId);

        return bookId;
    }

}
