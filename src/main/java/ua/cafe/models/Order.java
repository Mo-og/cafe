package ua.cafe.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.Range;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.*;

@JsonIgnoreProperties({"hibernateLazyInitializer"})
@Entity
@Table(name = "orders")
@Getter
@Setter
@DynamicUpdate
public class Order implements Comparable<Order> {
    public static final int TABLES_COUNT = 100;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(updatable = false, name = "date_ordered", nullable = false)
    private Date dateOrdered;
    private String comments;
    @Range(min = 1, max = TABLES_COUNT, message = "Номер столика должен быть в пределах от 1 до " + TABLES_COUNT + "!")
    @Column(name = "table_num", nullable = false)
    private int tableNum;
    private ReadyStatus status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.REMOVE)
    @ToString.Exclude
    private List<Detail> details = new ArrayList<>();

    public Order() {
    }

    //for report
    public void sortByQuantity() {
        if (details == null) return;
        details.sort((d1, d2) -> d2.getQuantity() == d1.getQuantity() ? (int) (d2.getCost() - d1.getCost()) : (d2.getQuantity() - d1.getQuantity()));
    }

    //for report
    public void sortByCost() {
        if (details == null) return;
        details.sort((d1, d2) -> (int) (d2.getCost() - d1.getCost()));
    }

    public Detail getDetailsIfPresent(long id) {
        return details.stream().filter(i -> i.getDishId() == id).findFirst().orElse(null);
    }

    public boolean removeFromOrder(long id) {
        Detail detail = details.stream().filter(i -> i.getDish().getId() == id
                || i.getDishId() == id).findFirst().orElse(null);
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

    public void addDetail(Detail detail) {
        if (details.contains(detail)) return;
        details.add(detail);
        detail.setOrder(this);
    }

    //for report only
    public void plainAddDetail(Detail detail) {
        details.add(detail);
    }

    public void stackDetail(Detail detail) {
        Optional<Detail> optionalDetail = details.stream()
                .filter(d -> d.getDishId() == detail.getDishId())
                .findFirst();
        optionalDetail.ifPresentOrElse(
                present -> present.setQuantity(present.getQuantity() + detail.getQuantity()),
                () -> details.add(detail)
        );
    }

    public String getDishNames() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Detail d : details) {
            if (d.getDish() == null) {
                stringBuilder.append(", null");
                continue;
            }
            stringBuilder.append(", ").append(d.getDish().getName().length() < 21 ? d.getDish().getName() : d.getDish().getName().substring(0, 20) + "...")
                    .append(" (").append(d.getQuantity()).append(" шт.)\n");
        }
        return stringBuilder.length() > 2 ? stringBuilder.substring(2) : stringBuilder.toString();
    }

    public List<Detail> getDetails() {
        return new ArrayList<>(details);
    }

    public List<Detail> acquireDetails() {
        return details;
    }

    public void retainDetails(List<Detail> list) {
        details.retainAll(list);
    }

    public double getCost() {
        return details.parallelStream().mapToDouble(Detail::getCost).sum();
    }

    public void updateStatus() {
        boolean hasNew = false;
        boolean hasReady = false;
        boolean hasInProgress = false;
        boolean hasFinished = false;
        for (var d : details) {
            switch (d.getStatus()) {
                case IN_PROGRESS -> hasInProgress = true;
                case NEW -> hasNew = true;
                case READY -> hasReady = true;
                case FINISHED -> hasFinished = true;
            }
        }

        if (hasReady) {
            status = ReadyStatus.READY;
        } else if (hasInProgress) {
            status = ReadyStatus.IN_PROGRESS;
        } else if (hasNew) {
            status = ReadyStatus.NEW;
        } else {
            status = hasFinished ? ReadyStatus.FINISHED : ReadyStatus.PAID;
        }
    }

    public ReadyStatus getStatus() {
        updateStatus();
        return status;
    }

    public String getStatusDescription() {
        return getStatus().getDescription();
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

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", dateOrdered=" + dateOrdered +
                ", comments='" + comments + '\'' +
                ", tableNum=" + tableNum +
                ", status='" + status + '\'' +
                ", details=" + details +
                '}';
    }

    @Override
    public int compareTo(@NotNull Order o) {
        if (tableNum == o.getTableNum())
            return Long.compare(dateOrdered.getTime(), o.getDateOrdered().getTime());
        return Integer.compare(tableNum, o.getTableNum());
    }
}