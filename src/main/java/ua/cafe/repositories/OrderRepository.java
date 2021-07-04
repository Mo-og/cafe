package ua.cafe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.cafe.entities.Order;

public interface OrderRepository extends JpaRepository<Order,Long> {
//    List<Order> findAllByOrderByTable_num();
}
