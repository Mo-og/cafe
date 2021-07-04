package ua.cafe.entities;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

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
    private String category;
    @NotBlank(message = "У блюда должно быть название!")
    private String name;
    @Range(min = 1, message = "Масса блюда не может быть меньше 1!")
    private double weight;
    private double price;
    private String ingredients;
    private boolean available = true;
    private String imagePath = null;

    @OneToMany(mappedBy = "dish", cascade = CascadeType.ALL)
    private List<Details> details;

    public void addDetail(Details detail) {
        for (Details d : details) {
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
