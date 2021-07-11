package ua.cafe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.cafe.entities.Detail;

public interface DetailsOfOrderedDishRepository extends JpaRepository<Detail, Long> {
    Detail findByOrder_idAndDish_id(long order_id, long dish_id);
}
