package ua.cafe.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
import ua.cafe.controllers.DishController;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Table(name = "dishes")
@Data
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
    private List<Detail> details;

    public void addDetail(Detail detail) {
        for (Detail d : details) {
            if (d.getOrder_id() != detail.getOrder_id())
                details.add(detail);
        }
    }

    public Dish() {
    }

    public Dish(long id) {
        this.id = id;
    }

    public Dish(@NotBlank(message = "У блюда должно быть название!") String name) {
        this.name = name;
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


}
