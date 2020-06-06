package ua.opu.kurs_gorbik_kozyrevych.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.opu.kurs_gorbik_kozyrevych.DishCategory;

import java.util.List;

public interface CategoriesRepository extends JpaRepository<DishCategory,Long> {
    List<DishCategory> findAllByOrderByName();
}
