package az.expressbank.category.data.repository;

import az.expressbank.category.data.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("select c from Category c where c.id=:id")
    List<Category> getCategoryById(@Param("id")Long id);
}
