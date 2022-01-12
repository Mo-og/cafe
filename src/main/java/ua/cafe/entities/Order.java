package ua.cafe.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import java.util.*;

@JsonIgnoreProperties({"hibernateLazyInitializer"})
@Entity
@Table(name = "orders")
@Getter
@Setter
@ToString
public class Order {
    public static final int TABLES_COUNT = 100;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private Date dateOrdered;
    private String comments;
    @Range(min = 1, max = TABLES_COUNT)
    private int tableNum;
    private String status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Detail> details;


    public Order() {
    }

    public Order(ArrayList<Detail> details) {
        this.details = details;
    }

    public void sortByQuantity() {
        if (details == null) return;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Order order = (Order) o;
        return id != 0 && Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}