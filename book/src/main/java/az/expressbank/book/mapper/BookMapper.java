package az.expressbank.book.mapper;

import az.expressbank.book.data.dto.BookDTO;
import az.expressbank.book.data.entity.Book;
import org.modelmapper.ModelMapper;

public class BookMapper {
    private static final ModelMapper modelMapper = new ModelMapper();

    public static Book mapDtoToEntity(BookDTO dto) {
        return modelMapper.map(dto, Book.class);
    }
}
