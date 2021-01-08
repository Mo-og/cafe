package ua.opu.kurs_gorbik_kozyrevych;

import lombok.Data;
import ua.opu.kurs_gorbik_kozyrevych.controllers.DishController;

import javax.persistence.*;
import java.util.*;

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
    private List<Details> details;


    public Order() {
    }

    public Order(ArrayList<Details> details) {
        this.details = details;
    }

    public void sortByQuantity() {
        details.sort(Comparator.comparingInt(Details::getQuantity).reversed());
    }

    public Details getDetailsIfPresent(long id) {
        return details.stream().filter(i -> i.getDish_id() == id).findFirst().orElse(null);
    }

    public boolean removeFromOrder(long id) {
        Details detail = details.stream().filter(i -> i.getDish().getId() == id
                || i.getDish_id() == id).findFirst().orElse(null);
        if (detail == null)
            throw new NoSuchElementException();
        System.out.println("Удаляем из заказа №" + id + ": " + detail);
        return details.remove(detail);
    }

    public void removeDetail(Details detail) {
        details.remove(detail);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void addDetail(Details detail) {
        Details present = getDetailsIfPresent(detail.getDish_id());
        if (present != null) {
            present.setQuantity(detail.getQuantity() + present.getQuantity());
            if (present.getQuantity() == 0)
                details.remove(detail);
            System.out.println("Изменяем количество блюд: quantity(" + present.getDish().getName() + ") = " + detail.getQuantity());
        } else {
            if (detail.getQuantity() == 0) {
                System.out.println("Количество блюда = 0 - пропускаем");
                return;
            }
            //detail.setOrder(this);
            DishController.getDishById(detail.getDish_id()).addDetail(detail);
            details.add(detail);
            System.out.println("Добавляем блюдо: (" + detail.getDish().getName() + ") в количестве: " + detail.getQuantity());
        }
    }

    public void techAddDetail(Details detail) {
        if (details == null) details = new ArrayList<>();
        Details present = getDetailsIfPresent(detail.getDish_id());
        if (present != null) {
            present.setQuantity(detail.getQuantity() + present.getQuantity());
            System.out.println("Увеличеваем количество блюд: quantity(" + present.getDish().getName() + ") = " + detail.getQuantity());

        } else {
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

    public List<Details> getDetails() {
        return details;
    }

    public double getCost() {
        double cost = 0;
        for (Details d : details) {
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