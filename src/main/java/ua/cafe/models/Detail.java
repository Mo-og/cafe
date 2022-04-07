package ua.cafe.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.cafe.services.DishService;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.util.Objects;

@Entity
@Table(name = "details")
@Getter
@Setter
@Component
public class Detail {
    private static DishService dishService;

    @Autowired
    public void setService(DishService service) {
        dishService = service;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Min(1)
    private long id;
    @Min(1)
    @Column(insertable = false, updatable = false, nullable = false, name = "dish_id")
    private Long dishId = -1L;
    @Min(1)
    @Column(insertable = false, updatable = false, nullable = false, name = "order_id")
    private Long orderId = -1L;
    private int quantity;
    private String comment;
    private ReadyStatus status = ReadyStatus.NEW;


    @ManyToOne(optional = false, cascade = CascadeType.MERGE)
    @JoinColumn(name = "dish_id")
    @JsonIgnore
    private Dish dish;

    @ManyToOne(optional = false, cascade = CascadeType.MERGE)
    @JoinColumn(name = "order_id")
    @JsonIgnore
    private Order order;

    public Detail(Dish dish, int quantity) {
        this.quantity = quantity;
        setDish(dish);
    }

    public Detail() {
    }

    public Detail(long dishId, int quantity) {
        dish = dishService.getById(dishId);
        if (dish == null)
            throw new IllegalArgumentException();
        dish.addDetail(this);
        this.dishId = dishId;
        this.quantity = quantity;
    }

    public Detail(long dishId, long orderId, int quantity) {
        this.dishId = dishId;
        this.orderId = orderId;
        this.quantity = quantity;
    }

    //Used when creating JSON
    public String getStatusDesc() {
        if (status == null)
            return null;
        return status.getDescription();
    }

    public Dish getDish() {
        //TODO: remove database interaction
        if (dish == null && dishId != -1) {
            dish = dishService.getById(dishId);
        }
        return dish;
    }

    //Preserves integrity
    public void setDish(Dish dish) {
        if (this.dish != null && this.dish.equals(dish))
            return;
        Dish oldDish = this.dish;
        this.dish = dish;
        if (oldDish != null) {
            oldDish.removeDetail(this);
        }
        if (dish != null) {
            dishId = dish.getId();
            dish.addDetail(this);
        }
    }

    //preserves integrity, dish must not be null
    public void setOrderRetrieveDish(Order order) {
        if (dish == null && dishId != -1)
            setDish(dishService.getById(dishId));
        if (dish == null)
            throw new IllegalStateException("Incorrect dish_id. Dish cannot be null but cannot be retrieved from DB.");
        setOrder(order);
    }

    public Order getOrder() {
        return order;
    }

    //preserves integrity
    public void setOrder(Order order) {
        if (this.order != null && this.order.equals(order))
            return;
        Order oldOrder = this.order;
        this.order = order;
        if (oldOrder != null) {
            oldOrder.removeDetail(this);
        }
        if (order != null) {
            orderId = order.getId();
            order.addDetail(this);
        }
    }

    public long getOrderId() {
        if (order == null)
            return orderId;
        return order.getId();
    }

    public long getDishId() {
        if (dish == null)
            return dishId;
        return dish.getId();
    }

    public double getCost() {
        if (dish == null)
            return -0.001;
        return dish.getPrice() * quantity;
    }

    @Override
    public String toString() {
        return "Detail{" +
                "dish_id=" + dishId + (dish == null ? "" : ('(' + dish.getName() + ')')) +
                ", order_id=" + orderId + (order == null ? "" : "(order obj set)") +
                ", id=" + id +
                ", quantity=" + quantity +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Detail detail = (Detail) o;
        return id == detail.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

