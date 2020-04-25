package ua.opu.kurs_gorbik_kozyrevych;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private Date date;
    private String comment;
    @OneToMany
    private List<Dish> dishes;

    public Order(Date date, String comment, List<Dish> dishes) {
        this.date = date;
        this.comment = comment;
        this.dishes = dishes;
    }

    public double getCost() {
        double cost=0;
        for (Dish d : dishes) {
            cost+=d.getPrice();
        }
        return cost;
    }


    public Order() {
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<Dish> getDishes() {
        return dishes;
    }

    public void setDishes(List<Dish> dishes) {
        this.dishes = dishes;
    }
}