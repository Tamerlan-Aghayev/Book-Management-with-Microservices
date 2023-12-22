package az.expressbank.category.service;

import az.expressbank.category.data.dto.CategoryDTO;
import az.expressbank.category.data.entity.Category;

import java.util.List;

public interface CategoryService {
     List<CategoryDTO> getAll();
     CategoryDTO getById(Long id);
     List<CategoryDTO> getByIds(List<Long> ids);
     Long addCategory(CategoryDTO categoryDTO);
}
