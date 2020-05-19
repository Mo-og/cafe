package ua.opu.kurs_gorbik_kozyrevych.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;
import ua.opu.kurs_gorbik_kozyrevych.Dish;

import java.util.List;

public interface DishRepository extends JpaRepository<Dish,Long> {
    List<Dish> findAllByOrderByCategory();
}
