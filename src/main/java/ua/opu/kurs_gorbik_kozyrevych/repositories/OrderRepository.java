package ua.opu.kurs_gorbik_kozyrevych.repositories;

import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import ua.opu.kurs_gorbik_kozyrevych.Dish;
import ua.opu.kurs_gorbik_kozyrevych.Order;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order,Long> {
}
