package az.expressbank.book.client;

import az.expressbank.book.data.dto.CategoryDTO;
import feign.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name="CategoryClient", url="http://localhost:8084/category")
public interface CategoryClient {
    @GetMapping("/category/{id}")
    Response getCategoryById(@PathVariable("id") Long id);
    @GetMapping("/category/ids")
    Response getCategoryByIds(@RequestParam List<Long> ids);
    @PostMapping("/category")
    Response addCategory(@RequestBody CategoryDTO categoryDTO);

}
