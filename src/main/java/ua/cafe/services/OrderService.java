package ua.cafe.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import ua.cafe.entities.Order;
import ua.cafe.repositories.OrderRepository;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Lazy
@Service
public class OrderService {

    private OrderRepository repository;

    @Autowired
    public void setRepository(OrderRepository repository) {
        this.repository = repository;
    }

    public Order saveOrder(Order order) {
        return repository.save(order);
    }

//    public List<Order> getAllOrders() {
//        return repository.findAllByOrderByTable_num();
//    }

    //TODO надо решить как-то через репозиторий методом вроде findAllByOrderByTable_numAndDate_ordered()
    // **решается прерименовыванием table_num в tableNum и date_ordered в dateOrdered и написанием метода в репозитории,
    // но надо отследить изменение имён
    public List<Order> getAllOrders() {
        return repository.findAllOrderedByTableNum();
        /*List<Order> n = repository.findAll();
        n.sort(comparator);
        return n;*/
    }

    Comparator<Order> comparator = (o1, o2) -> {
        if (o1.getTableNum() == o2.getTableNum())
            return Long.compare(o1.getDateOrdered().getTime(), o2.getDateOrdered().getTime());
        return Long.compare(o1.getTableNum(), o2.getTableNum());
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

    public Date getDateOrdered(long id){
        return repository.getDateOrdered(id);
    }

}