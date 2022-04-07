package ua.cafe.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.validator.constraints.Range;
import org.jetbrains.annotations.NotNull;
import ua.cafe.utils.Stats;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "dishes")
@Getter
@Setter
@RequiredArgsConstructor
public class Dish implements Comparable<Dish> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @ManyToOne
    private Category category;
    @NotBlank(message = "У блюда должно быть название!")
    private String name;
    @Range(min = 1, message = "Масса блюда не может быть меньше 1!")
    private double weight;
    private double price;
    private String ingredients;
    private boolean available = true;
    private String imagePath = null;
    @Transient
    @JsonIgnore
    private boolean isThumb = false;

    @OneToMany(mappedBy = "dish", cascade = CascadeType.ALL)
    @JsonIgnore
    @ToString.Exclude
    private List<Detail> details = new ArrayList<>();

    public void addDetail(Detail detail) {
        if (details.contains(detail)) return;
        details.add(detail);
        detail.setDish(this);
    }

    public List<Detail> getDetails() {
        return new ArrayList<>(details);
    }

    public void removeDetail(Detail detail) {
        if (!details.contains(detail)) return;
        details.remove(detail);
        detail.setDish(null);
    }

    public String getImagePath() {
        if (isThumb)
            if (imagePath != null && imagePath.length() > 11)
                return Stats.URL_THUMBNAILS_PATH + imagePath.substring(11);
        return imagePath;
    }

    @JsonIgnore
    public String getClearImagePath() {
        return imagePath;
    }

    public Category getCategory() {
        return category;
    }

    public long getCategoryId() {
        return category.getId();
    }

    @Override
    public String toString() {
        return "Dish{" +
                "id=" + id +
//                ", category='" + category + '\'' +
                ", name='" + name + '\'' +
//                ", weight=" + weight +
//                ", ingredients='" + ingredients + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Dish dish = (Dish) o;
        return id != 0 && Objects.equals(id, dish.id);
    }

    @Override
    public int compareTo(@NotNull Dish o) {
        int result = this.category.compareTo(o.category);
        if (result == 0) {
            result = this.name.compareToIgnoreCase(o.name);
        }
        return result;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
