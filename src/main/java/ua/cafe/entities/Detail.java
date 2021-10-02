package ua.cafe.entities;

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
    @Column(insertable = false, updatable = false)
    @Min(1)
    private long dish_id = -1;
    @Column(insertable = false, updatable = false)
    @Min(1)
    private long order_id = -1;
    private int quantity;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "dish_id")
    private Dish dish;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    @JsonIgnore
    private Order order;

    public Detail(Dish dish, int quantity) {
        this.quantity = quantity;
        this.dish = dish;
    }

    public Detail() {
    }

    public Detail(long dish_id, long order_id, int quantity, Dish dish) {
        this.dish_id = dish_id;
        this.order_id = order_id;
        this.quantity = quantity;
        this.dish = dish;
    }

    public Detail(long dish_id, long order_id, int quantity) {
        this.dish_id = dish_id;
        this.order_id = order_id;
        this.quantity = quantity;
        /*try {
            this.dish = DishController.getDishById(dish_id);
        } catch (Exception e) {*/
        this.dish = null;
//        }
    }

    public void setDish_id(long dish_id) {
        this.dish_id = dish_id;
    }

    public void setOrder_id(long order_id) {
        this.order_id = order_id;
    }

    public Dish getDish() {
        //TODO: remove database interaction
        if (dish == null && dish_id!=-1) {
            dish = dishService.getById(dish_id);
        }
        return dish;
    }

    public void setDish(Dish dish) {
        this.dish = dish;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public long getOrder_id() {
        if (order == null)
            return order_id;
        return order.getId();
    }

    public long getDish_id() {
        if (dish == null)
            return dish_id;
        return dish.getId();
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getCost() {
        if (dish == null)
            return -0.001;
        return dish.getPrice() * quantity;
    }

    @Override
    public String toString() {
        return "Detail{" +
                "dish_id=" + dish_id +
                ", order_id=" + order_id +
                ", id=" + id +
                ", quantity=" + quantity +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Detail detail = (Detail) o;
        return id == detail.id && dish_id == detail.dish_id && order_id == detail.order_id && quantity == detail.quantity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, dish_id, order_id, quantity);
    }
}

