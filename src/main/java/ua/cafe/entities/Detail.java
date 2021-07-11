package ua.cafe.entities;

import lombok.Data;
import ua.cafe.controllers.DishController;

import javax.persistence.*;

@Entity
@Table(name = "details")
@Data
public class Detail {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(insertable = false, updatable = false)
    private long dish_id = -1;
    @Column(insertable = false, updatable = false)
    private long order_id = -1;
    private int quantity;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "dish_id")
    private Dish dish;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
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
            return -0.1;
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
}

