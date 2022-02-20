package ua.cafe.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import ua.cafe.entities.Detail;
import ua.cafe.entities.Order;
import ua.cafe.repositories.DetailsOfOrderedDishRepository;
import ua.cafe.repositories.OrderRepository;

import java.util.Date;
import java.util.List;

@Lazy
@Service
public class OrderService {

    private OrderRepository repository;
    private DetailsOfOrderedDishRepository detailsRepository;

    @Autowired
    public void setRepository(OrderRepository repository) {
        this.repository = repository;
    }

    @Autowired
    public void setRepository(DetailsOfOrderedDishRepository repository) {
        this.detailsRepository = repository;
    }

    public Order saveOrder(Order order) {
        return repository.save(order);
    }

    public Order saveNewOrder(Order order) {
        order.setDateOrdered(new Date(System.currentTimeMillis()));
        Order finalOrder = repository.save(order);
        order.getDetails().forEach(detail -> detail.setOrderRetrieveDish(finalOrder));
        detailsRepository.saveAll(order.getDetails());
        finalOrder.setDetails(order.getDetails());
        return finalOrder;
    }

    public List<Order> getAllOrders() {
        return repository.findAllOrderedByTableNum();
    }

    public void deleteById(long id) {
        repository.deleteById(id);
    }

    public boolean existsWithId(long id) {
        return repository.existsById(id);
    }

    public Order getById(long id) {
        return repository.getOne(id);
    }

    public Date findDateOrdered(long id) {
        return repository.findDateOrdered(id);
    }

    public Order updateOrder(Order orderUpdate) {
        Order fromDb = getById(orderUpdate.getId());
        fromDb.setTableNum(orderUpdate.getTableNum());
        fromDb.setComments(orderUpdate.getComments());
        //////
        //TODO: remove detail by API [delete detail] instead of this
        List<Detail> details = fromDb.getDetails();
        details.removeAll(orderUpdate.getDetails());
        detailsRepository.deleteAll(details);
        //////
        orderUpdate.getDetails().forEach(detail -> {
            if (detail.getId() != 0) {
                Detail d = detailsRepository.getOne(detail.getId());
                d.setQuantity(detail.getQuantity());
                d.setStatus(detail.getStatus());
            } else {
                detail.setOrderRetrieveDish(fromDb);
                detailsRepository.save(detail);
            }
        });
        fromDb.setDetails(orderUpdate.getDetails());
        fromDb.updateStatus();
        return repository.save(fromDb);
    }
}