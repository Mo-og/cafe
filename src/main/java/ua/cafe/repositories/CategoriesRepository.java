package ua.cafe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.cafe.entities.DishCategory;

import java.util.List;

public interface CategoriesRepository extends JpaRepository<DishCategory,Long> {
    List<DishCategory> findAllByOrderByName();
}
