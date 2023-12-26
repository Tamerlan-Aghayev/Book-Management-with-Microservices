package az.expressbank.category.mapper;

import az.expressbank.category.data.dto.CategoryDTO;
import az.expressbank.category.data.entity.Category;
import org.mapstruct.Mapper;
import org.modelmapper.ModelMapper;
@Mapper(componentModel="spring")
public interface CategoryMapper {
    Category DTOToEntity(CategoryDTO categoryDTO);
    CategoryDTO entityToDTO(Category category);
}
