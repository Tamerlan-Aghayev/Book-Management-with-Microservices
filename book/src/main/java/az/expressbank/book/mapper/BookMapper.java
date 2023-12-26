package az.expressbank.book.mapper;

import az.expressbank.book.data.dto.BookDTO;
import az.expressbank.book.data.entity.Book;
import org.mapstruct.Mapper;
import org.modelmapper.ModelMapper;
@Mapper(componentModel = "spring")
public interface BookMapper {
    Book DTOToEntity(BookDTO bookDTO);
    BookDTO entityToDTO(Book book);
}
