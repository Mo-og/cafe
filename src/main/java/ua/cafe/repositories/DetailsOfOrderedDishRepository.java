package ua.cafe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.cafe.entities.Details;

public interface DetailsOfOrderedDishRepository extends JpaRepository<Details, Long> {
    Details findByOrder_idAndDish_id(long order_id, long dish_id);
}
