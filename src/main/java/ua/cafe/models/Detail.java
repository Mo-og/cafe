package ua.cafe.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.cafe.services.DishService;
import ua.cafe.services.OrderService;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.util.Objects;

@Entity
@Table(name = "details")
@Getter
@Setter
@Component
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class Detail {
    @JsonIgnore
    private static DishService dishService;
    @JsonIgnore
    private static OrderService orderService;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne(optional = false, cascade = CascadeType.MERGE)
    @JoinColumn(name = "dish_id")
    @JsonIgnore
    private Dish dish;
    @ManyToOne(optional = false, cascade = CascadeType.MERGE)
    @JoinColumn(name = "order_id")
    @JsonIgnore
    private Order order;

    @Min(1)
    @Column(insertable = false, updatable = false, nullable = false, name = "dish_id")
    private Long dishId = -1L;
    @Min(1)
    @Column(insertable = false, updatable = false, nullable = false, name = "order_id")
    private Long orderId = -1L;
    private int quantity;
    private String comment;
    private ReadyStatus status = ReadyStatus.NEW;

    @Autowired
    @JsonIgnore
    public void setService(DishService service) {
        dishService = service;
    }

    @Autowired
    @JsonIgnore
    public void setService(OrderService service) {
        orderService = service;
    }

    public Detail(Dish dish, int quantity) {
        this.quantity = quantity;
        setDish(dish);
    }

    public Detail() {
    }

    public Detail(long dishId, long orderId, int quantity) {
        this.dishId = dishId;
        this.orderId = orderId;
        this.quantity = quantity;
    }

    //for report only
    public Detail(long dishIdAndDetailId) {
        id = dishIdAndDetailId;
        this.dishId = dishIdAndDetailId;
    }

    public ReadyStatus getStatus() {
        if (status == null)
            status = ReadyStatus.NEW;
        return status;
    }

    //Used when creating JSON
    public String getStatusDesc() {
        return getStatus().getDescription();
    }

    public Dish getDish() {
        //TODO: remove database interaction
        if (dish == null && dishId != -1) {
            if (dishService == null) {
                dish = new Dish();
                dish.setPrice(dishId);
            } else
                dish = dishService.getById(dishId);
        }
        return dish;
    }

    public void persist() {
        if (dishId == -1 || orderId == -1) {
            throw new IllegalStateException("Cannot persist because dishId or orderId is not set.");
        }
        if (order == null) {
            setOrder(orderService.getById(orderId));
        }
        if (dish == null) {
            setDish(dishService.getById(dishId));
        }
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
        } else dishId = -1L;
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
        } else orderId = -1L;
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
        if (dishId == -1)
            return -0.0;
        return getDish().getPrice() * quantity;
    }

    @Override
    public String toString() {
        return "Detail{" +
                "dishId=" + dishId + (dish == null ? "" : ('(' + dish.getName() + ')')) +
                ", orderId=" + orderId + (order == null ? "" : "(order obj set)") +
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

