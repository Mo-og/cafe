package ua.opu.kurs_gorbik_kozyrevych.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.opu.kurs_gorbik_kozyrevych.Dish;

public interface DishRepository extends JpaRepository<Dish,Long> {
}
