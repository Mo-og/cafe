package ua.cafe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.cafe.models.Order;

import java.util.Date;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> getOrdersByDateOrderedBetween(Date dateOrderedFrom, Date dateOrderedTo);

    @Query("SELECT dateOrdered FROM Order where id=:id")
    Date findDateOrdered(@Param("id") long order_id);

    @Query(value = "SELECT * FROM orders o ORDER BY o.table_num, o.date_ordered", nativeQuery = true)
    List<Order> findAllOrderedByTableNum();
}
