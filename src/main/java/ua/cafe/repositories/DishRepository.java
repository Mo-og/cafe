package ua.cafe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.cafe.models.Dish;

public interface DishRepository extends JpaRepository<Dish, Long> {
    //List<Dish> findAllByOrderByCategoryId();
}
