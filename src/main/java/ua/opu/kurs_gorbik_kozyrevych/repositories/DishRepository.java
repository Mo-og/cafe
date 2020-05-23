package ua.opu.kurs_gorbik_kozyrevych.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.opu.kurs_gorbik_kozyrevych.Dish;

import java.util.List;

public interface DishRepository extends JpaRepository<Dish, Long> {
    List<Dish> findAllByOrderByCategory();
}
