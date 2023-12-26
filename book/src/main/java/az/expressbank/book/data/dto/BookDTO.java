package az.expressbank.book.data.dto;

import az.expressbank.book.data.entity.Book;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {
    private String name;
    private String author;
    private CategoryDTO categoryDTO;

}
