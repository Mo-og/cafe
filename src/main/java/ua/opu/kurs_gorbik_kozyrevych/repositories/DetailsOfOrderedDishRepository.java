package ua.opu.kurs_gorbik_kozyrevych.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.opu.kurs_gorbik_kozyrevych.Details;

public interface DetailsOfOrderedDishRepository extends JpaRepository<Details, Long> {
    Details findByOrder_idAndDish_id(long order_id, long dish_id);
}
