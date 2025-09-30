package testSaveMp.testSaveMp.server.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import testSaveMp.testSaveMp.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    Optional<Category> findByName(String categoryName);
    @Query("SELECT c.name FROM Category c WHERE c.name LIKE %:query%") //Вот это не правильно, оно работет, но за N0-1 скорость, это медленно + нету базовой развертки по типу... параметров страницы
    List<String> searchCategoryNames(@Param("query") String query, Pageable pageable);

    @Query("SELECT c.name FROM Category c")
    List<String> findAllName();
}
