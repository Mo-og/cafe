package ua.cafe.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.validator.constraints.Range;
import ua.cafe.controllers.DishController;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "dishes")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Dish {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private long categoryId;
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
    private List<Detail> details=new ArrayList<>();

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
                return DishController.URL_THUMBNAILS_PATH + imagePath.substring(11);
        return imagePath;
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
    public int hashCode() {
        return getClass().hashCode();
    }
}
