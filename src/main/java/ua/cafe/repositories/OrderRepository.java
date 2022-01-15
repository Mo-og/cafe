package ua.cafe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.cafe.entities.Order;

import java.util.Date;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT dateOrdered FROM Order where id=:id")
    Date getDateOrdered(@Param("id") long order_id);

    @Query(value = "SELECT o FROM Order o ORDER BY o.tableNum, o.dateOrdered ASC")
//    @Query(value = "SELECT * FROM orders ORDER BY table_num, date_ordered ASC", nativeQuery = true)
    List<Order> findAllOrderedByTableNum();
}
