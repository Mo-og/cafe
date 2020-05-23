package ua.opu.kurs_gorbik_kozyrevych;

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


    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
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
