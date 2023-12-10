package ua.cafe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ua.cafe.models.Dish;

public interface DishRepository extends JpaRepository<Dish, Long> {

  @Query("SELECT MIN(d.id) FROM Dish d")
  Long findMinId();
}
