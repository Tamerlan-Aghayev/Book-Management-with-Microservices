package az.expressbank.book.mapper;

import az.expressbank.book.data.dto.BookDTO;
import az.expressbank.book.data.entity.Book;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.modelmapper.ModelMapper;

import java.lang.annotation.Target;

@Mapper(componentModel = "spring")
public interface BookMapper {

    @Mapping(source = "categoryDTO", target = "categoryId", ignore = true)
    Book DTOToEntity(BookDTO bookDTO);

    BookDTO entityToDTO(Book book);
}
