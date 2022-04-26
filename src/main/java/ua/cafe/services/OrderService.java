package ua.cafe.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import ua.cafe.models.Detail;
import ua.cafe.models.Order;
import ua.cafe.repositories.DetailRepository;
import ua.cafe.repositories.DishRepository;
import ua.cafe.repositories.OrderRepository;

import java.util.Date;
import java.util.List;

@Lazy
@Service
@Slf4j
public class OrderService {

    private OrderRepository repository;
    private DetailRepository detailRepository;
    private DishRepository dishRepository;

    @Autowired
    public void setRepository(OrderRepository repository) {
        this.repository = repository;
    }

    @Autowired
    public void setRepository(DetailRepository repository) {
        this.detailRepository = repository;
    }

    @Autowired
    public void setRepository(DishRepository repository) {
        this.dishRepository = repository;
    }

    public Order saveOrder(Order order) {
        return repository.save(order);
    }

    public Order saveNewOrder(Order order) {
        order.setDateOrdered(new Date(System.currentTimeMillis()));
        Order finalOrder = repository.save(order);
        order.getDetails().forEach(detail -> detail.setOrderRetrieveDish(finalOrder));
        detailRepository.saveAll(order.getDetails());
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
        detailRepository.deleteAll(details);
        //////
        orderUpdate.getDetails().forEach(detail -> {
            if (detail.getId() != 0) {
                Detail d = detailRepository.getOne(detail.getId());
                d.setQuantity(detail.getQuantity());
                d.setStatus(detail.getStatus());
            } else {
                detail.setOrderRetrieveDish(fromDb);
                detailRepository.save(detail);
            }
        });
        fromDb.setDetails(orderUpdate.getDetails());
        fromDb.updateStatus();
        return repository.save(fromDb);
    }

    public Order getReportOrder(Date from, Date to, boolean sortByQuantity, boolean includeZeros) {
        if (from.getTime() > to.getTime()) {
            Date temp = from;
            from = to;
            to = temp;
        }

        log.info("\nMaking report for period\nfrom:\n" + from + "\nto:\n" + to + "\nsortByQuantity: " + sortByQuantity + "\nincludeZeros: " + includeZeros + "\n");

        List<Order> orders = repository.getOrdersByDateOrderedBetween(from, to);

        Order order = new Order();
        // We need to add all existing dishes (with quantity of 0) to see which ones were not ordered,
        // otherwise we won't be able to tell which dishes had zero success
        if (includeZeros)
            dishRepository.findAll().forEach(dish -> order.plainAddDetail(new Detail(dish.getId())));

        for (Order o : orders) {
            for (Detail d : o.getDetails()) {
                order.stackDetail(d);
            }
        }

        if (sortByQuantity) order.sortByQuantity();
        else order.sortByCost();

        return order;
    }
}