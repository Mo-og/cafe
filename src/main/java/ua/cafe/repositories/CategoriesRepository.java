package ua.cafe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.cafe.models.Category;

import java.util.List;

public interface CategoriesRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByOrderByName();

    Category getById(long categoryId);
}
