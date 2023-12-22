package az.expressbank.category.mapper;

import az.expressbank.category.data.dto.CategoryDTO;
import az.expressbank.category.data.entity.Category;
import org.modelmapper.ModelMapper;

public class CategoryMapper {

    private static final ModelMapper modelMapper=new ModelMapper();



    public static Category convertToEntity(CategoryDTO categoryDTO) {
        return modelMapper.map(categoryDTO, Category.class);
    }
}
