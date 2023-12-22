package az.expressbank.book.service;

import az.expressbank.book.data.dto.BookDTO;

import java.util.List;

public interface BookService {
    public List<BookDTO> getAll() throws Exception;
    public BookDTO getById(Long id) throws Exception;
    public Long addBook(BookDTO bookDTO) throws Exception;
    }
