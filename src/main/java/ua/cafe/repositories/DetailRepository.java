package ua.cafe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.cafe.models.Detail;

import java.util.List;

public interface DetailRepository extends JpaRepository<Detail, Long> {
    Detail findByOrder_idAndDish_id(long order_id, long dish_id);

    List<Detail> findDetailsByOrder_id(long order_id);
}
