package az.expressbank.category.controller;

import az.expressbank.category.data.dto.CategoryDTO;
import az.expressbank.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/category")

public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/category/{id}")
    public ResponseEntity<CategoryDTO> getById(@PathVariable("id") Long id){
        CategoryDTO categoryDTO=categoryService.getById(id);
        return ResponseEntity.ok(categoryDTO);
    }
    @GetMapping("/category/ids")
    public ResponseEntity<List<CategoryDTO>> getByIds(@RequestParam List<Long> ids){
        List<CategoryDTO> categoryDTOs=categoryService.getByIds(ids);
        return ResponseEntity.ok(categoryDTOs);
    }
    @GetMapping("/categories")
    public ResponseEntity<List<CategoryDTO>> getAll(){
        List<CategoryDTO> dtos=categoryService.getAll();
        return ResponseEntity.ok(dtos);
    }
    @PostMapping("/category")
    public ResponseEntity<Long> addCategory(@RequestBody CategoryDTO categoryDTO){
        Long id =categoryService.addCategory(categoryDTO);
        return ResponseEntity.ok(id);
    }
}
