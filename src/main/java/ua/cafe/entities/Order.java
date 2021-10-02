package ua.cafe.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.util.*;
@JsonIgnoreProperties({"hibernateLazyInitializer"})
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
    private String status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<Detail> details;


    public Order() {
    }

    public Order(ArrayList<Detail> details) {
        this.details = details;
    }

    public void sortByQuantity() {
        details.sort(Comparator.comparingInt(Detail::getQuantity).reversed());
    }

    public Detail getDetailsIfPresent(long id) {
        return details.stream().filter(i -> i.getDish_id() == id).findFirst().orElse(null);
    }

    public boolean removeFromOrder(long id) {
        Detail detail = details.stream().filter(i -> i.getDish().getId() == id
                || i.getDish_id() == id).findFirst().orElse(null);
        if (detail == null)
            throw new NoSuchElementException();
        System.out.println("Удаляем из заказа №" + id + ": " + detail);
        return details.remove(detail);
    }

    public void removeDetail(Detail detail) {
        details.remove(detail);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void addDetail(Detail detail) {
        if (details == null) details = new ArrayList<>();
        Detail present = getDetailsIfPresent(detail.getDish_id());
        if (present != null) {
            present.setQuantity(detail.getQuantity() + present.getQuantity());
            if (present.getDish() != null)
                System.out.println("Увеличеваем количество блюд: quantity(" + present.getDish().getName() + ") = " + detail.getQuantity());

        } else {
            details.add(detail);
            if (detail.getDish() != null)
                System.out.println("Добавляем блюдо: (" + detail.getDish().getName() + ") в количестве: " + detail.getQuantity());
        }
    }

    public String getDishNames() {
        String dishNames = "";
        for (Detail d : details) {
            dishNames = dishNames.concat(", " + d.getDish().getName() + " (" + d.getQuantity() + " шт.)");
        }
        dishNames = dishNames.replaceFirst(", ", "");
        return dishNames;
    }

    public List<Detail> getDetails() {
        return details;
    }

    public double getCost() {
        double cost = 0;
        for (Detail d : details) {
            cost += d.getCost();
        }
        return cost;
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