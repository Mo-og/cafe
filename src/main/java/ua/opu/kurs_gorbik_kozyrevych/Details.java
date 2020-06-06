package ua.opu.kurs_gorbik_kozyrevych;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "details")
@Data
public class Details {
    @Column(insertable = false, updatable = false)
    private long dish_id=-1;
    @Column(insertable = false, updatable = false)
    private long order_id=-1;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "dish_id")
    private Dish dish;


    public void setDish_id(long dish_id) {
        this.dish_id = dish_id;
    }

    public void setOrder_id(long order_id) {
        this.order_id = order_id;
    }

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private Order order;

    private int quantity;

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
        if(order==null)
            return order_id;
        return order.getId();
    }


    public long getDish_id() {
        if(dish==null)
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
        return dish.getPrice() * quantity;
    }


    public Details() {
    }

    public Details(Dish dish, int quantity, Order order) {
        this.dish = dish;
        this.order = order;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Details{" +
                "dish_id=" + dish_id +
                ", order_id=" + order_id +
                ", id=" + id +
                ", quantity=" + quantity +
                '}';
    }
}

