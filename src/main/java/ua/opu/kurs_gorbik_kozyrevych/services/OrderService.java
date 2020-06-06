package ua.opu.kurs_gorbik_kozyrevych.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.opu.kurs_gorbik_kozyrevych.Order;
import ua.opu.kurs_gorbik_kozyrevych.repositories.OrderRepository;

import java.util.Comparator;
import java.util.List;

@Service
public class OrderService {

    private OrderRepository repository;

    @Autowired
    public void setRepository(OrderRepository repository) {
        this.repository = repository;
    }

    public void saveOrder(Order order) {
        repository.save(order);
    }

//    public List<Order> getAllOrders() {
//        return repository.findAllByOrderByTable_num();
//    }

    //TODO надо решить как-то через репозиторий методом вроде findAllByOrderByTable_numAndDate_ordered()
    public List<Order> getAllOrders() {
        List<Order> n = repository.findAll();
        n.sort(comparator);
        return n;
    }

    Comparator<Order> comparator = (o1, o2) -> {
        if (o1.getTable_num() == o2.getTable_num()) {
            if (o1.getDate_ordered().getTime() < o2.getDate_ordered().getTime())
                return -1;
            return 1;
        }
        if (o1.getTable_num() < o2.getTable_num())
            return -1;
        return 1;
    };


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