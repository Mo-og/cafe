package ua.cafe.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.validator.constraints.Range;
import org.jetbrains.annotations.NotNull;
import ua.cafe.utils.Stats;

@Entity
@Table(name = "dishes")
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class Dish implements Comparable<Dish> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
  @Getter
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

  public Dish(Category category, String name, int weight, double price, String ingredients) {
    this.category = category;
    this.name = name;
    this.weight = weight;
    this.price = price;
    this.ingredients = ingredients;
  }

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
