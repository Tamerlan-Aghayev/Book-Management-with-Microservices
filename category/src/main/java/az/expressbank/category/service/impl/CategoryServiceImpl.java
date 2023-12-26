package az.expressbank.category.service.impl;

import az.expressbank.category.data.dto.CategoryDTO;
import az.expressbank.category.data.entity.Category;
import az.expressbank.category.data.repository.CategoryRepository;
import az.expressbank.category.exception.EntityParsingException;
import az.expressbank.category.exception.NotFoundException;
import az.expressbank.category.mapper.CategoryMapper;
import az.expressbank.category.service.CategoryService;
import ch.qos.logback.core.spi.LifeCycle;
import jakarta.ws.rs.core.Response;
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
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public List<CategoryDTO> getAll(){
        log.info("Fetching all categories");

        List<Category> categories= categoryRepository.findAll();
        List<CategoryDTO> dtos=new ArrayList<>();
        for (Category category:categories){
            dtos.add(categoryMapper.entityToDTO(category));
        }
        log.trace("Categories: {}" , dtos);
        log.info("Successfully fetched all categories");

        return dtos;
    }
    public CategoryDTO getById(Long id){
        log.info("Fetching category by ID: {}", id);

        List<Category> categories= categoryRepository.getCategoryById(Long.valueOf(id));
        if(categories.size()==1) {
            CategoryDTO dto=categoryMapper.entityToDTO(categoryRepository.getCategoryById(Long.valueOf(id)).get(0));
            log.trace("Category: {}" ,dto);

            log.info("Successfully fetched category by ID: {}", id);

            return dto;

        }
        else {
            log.error("Category with id {} doesn't exist", id);
            throw new NotFoundException("Category with id " + id + " doesn't exist");
        }
    }

    @Override
    public List<CategoryDTO> getByIds(List<Long> ids) {
        log.info("Fetching categories by IDs: {}", ids);

        List<Category> categories = categoryRepository.findAllById(ids);
        List<CategoryDTO> dtos = categories.stream()
                .map(i->categoryMapper.entityToDTO(i))
                .collect(Collectors.toList());
        log.trace("Categories: {}" , dtos);

        if (categories.size()==ids.size()){

            log.info("Successfully fetched categories by IDs: {}", ids);


            return dtos;
        }
        else {
            log.error("Some IDs in {} don't exist", ids);

            throw new NotFoundException("some IDs doesn't exist");
        }
    }

    public Long addCategory(CategoryDTO categoryDTO){
        log.info("Adding a new category");

        try {
            Category category = categoryMapper.DTOToEntity(categoryDTO);
            Long id=categoryRepository.saveAndFlush(category).getId();
            log.info("Successfully added a new category with id {}", id);

            return id;
        }catch (Exception ex){
            log.error("Error occurred while parsing DTO to entity", ex);

            throw new EntityParsingException("Exception occurred while parsing DTO to entity");
        }

    }

}

