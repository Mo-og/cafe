package ua.opu.kurs_gorbik_kozyrevych;

import lombok.Data;
import ua.opu.kurs_gorbik_kozyrevych.controllers.DishController;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private Date date_ordered;
    private String comments;
    private int table_num;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<Details> details;


    public Order() {
    }


    public Details getDetailsIfPresent(long id) {
        return details.stream().filter(i -> i.getDish_id() == id).findFirst().orElse(null);
    }

    public boolean removeFromOrder(long id) {
        Details detail = details.stream().filter(i -> i.getDish().getId() == id || i.getDish_id() == id).findFirst().orElse(null);
        System.out.println("Удаляем из заказа №" + id + ": " + detail);
        return details.remove(detail);
    }

    public void removeDetail(Details detail) {
        details.remove(detail);
    }

    public void addDetail(Details detail) {
        Details present = getDetailsIfPresent(detail.getDish_id());
        if (present != null) {
            present.setQuantity(detail.getQuantity() + present.getQuantity());
            System.out.println("Увеличеваем количество блюд: quantity(" + present.getDish().getName() + ") = " + detail.getQuantity());

        } else {
            //detail.setOrder(this);
            DishController.getDishById(detail.getDish_id()).addDetail(detail);
            details.add(detail);
            System.out.println("Добавляем блюдо: (" + detail.getDish().getName() + ") в количестве: " + detail.getQuantity());
        }
    }

    public String getDishNames() {
/** Использовать toString к details противопоказано - будет переполнение стека и метод сломается*/
        String dishNames = "";
        for (Details d : details) {
            dishNames = dishNames.concat(", " + d.getDish().getName() + " (" + d.getQuantity() + " шт.)");
        }
        dishNames = dishNames.replaceFirst(", ", "");
        return dishNames;
    }

    public int getTable_num() {
        return table_num;
    }

    public void setTable_num(int table) {
        this.table_num = table;
    }

    public double getCost() {
        double cost = 0;
        for (Details d : details) {
            cost += d.getCost();
        }
        return cost;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comment) {
        this.comments = comment;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDate_ordered() {
        return date_ordered;
    }

    public void setDate_ordered(Date date) {
        this.date_ordered = date;
    }


    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", date_ordered=" + date_ordered +
                ", comments='" + comments + '\'' +
                ", table_num=" + table_num +
                ", details=" + details +
                '}';
    }
}