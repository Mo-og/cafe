package ua.cafe.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import java.util.*;

@JsonIgnoreProperties({"hibernateLazyInitializer"})
@Entity
@Table(name = "orders")
@Getter
@Setter
@ToString
@DynamicUpdate
public class Order {
    public static final int TABLES_COUNT = 100;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(updatable = false, name = "date_ordered", nullable = false)
    private Date dateOrdered;
    private String comments;
    @Range(min = 1, max = TABLES_COUNT, message = "Номер столика должен быть в пределах от 1 до "+TABLES_COUNT+"!")
    @Column(name = "table_num", nullable = false)
    private int tableNum;
    private String status;

    @OneToMany(mappedBy = "order")
    @ToString.Exclude
    private List<Detail> details = new ArrayList<>();


    public Order() {
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
        if (!details.contains(detail)) return;
        details.remove(detail);
        detail.setOrder(null);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void addDetail(Detail detail) {
        if (details.contains(detail)) return;
        /*Detail present = getDetailsIfPresent(detail.getDish_id());
        if (present != null) {
            present.setQuantity(detail.getQuantity() + present.getQuantity());
            if (present.getDish() != null)
                System.out.println("Увеличеваем количество блюд: quantity(" + present.getDish().getName() + ") = " + detail.getQuantity());

        } else {
            details.add(detail);
            if (detail.getDish() != null)
                System.out.println("Добавляем блюдо: (" + detail.getDish().getName() + ") в количестве: " + detail.getQuantity());
        }*/
        details.add(detail);
        detail.setOrder(this);
    }

    public String getDishNames() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Detail d : details) {
            stringBuilder.append(", ").append(d.getDish().getName().length() < 21 ? d.getDish().getName() : d.getDish().getName().substring(0, 20) + "...")
                    .append(" (").append(d.getQuantity()).append(" шт.)\n");
        }
        return stringBuilder.length() > 2 ? stringBuilder.substring(2) : stringBuilder.toString();
    }

    public List<Detail> getDetails() {
        return new ArrayList<>(details);
    }

    public double getCost() {
        return details.parallelStream().mapToDouble(Detail::getCost).sum();
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