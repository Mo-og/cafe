package ua.opu.kurs_gorbik_kozyrevych.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.opu.kurs_gorbik_kozyrevych.Dish;
import ua.opu.kurs_gorbik_kozyrevych.Order;
import ua.opu.kurs_gorbik_kozyrevych.repositories.OrderRepository;

import java.util.List;

@Service
public class OrderService {

    private OrderRepository repository;

    @Autowired
    public void setRepository(OrderRepository repository) {
        this.repository = repository;
    }

    public void saveOrder(Order order){
    repository.save(order);
    }

    public List<Order> getAllOrders() {
        return repository.findAll();
    }

    public void removeById(long id) {
        repository.deleteById(id);
    }

    public boolean existsWithId(long id) {
        return repository.existsById(id);
    }

    public Order getById(long id) {
        return repository.getOne(id);
    }

}