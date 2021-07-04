package ua.cafe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.cafe.entities.Dish;

import java.util.List;

public interface DishRepository extends JpaRepository<Dish, Long> {
    //List<Dish> findAllByOrderByCategoryId();
}
