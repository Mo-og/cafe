package ua.opu.kurs_gorbik_kozyrevych;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;

@Entity
@Table(name = "dishCategory")
public class DishCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
//    @Pattern(regexp = "[ A-Za-zА-Яа-яЁё]{2,200}", message = "Название категории должно содержать только буквы (латинские или русские) и быть в пределах 2-200 символов.")
    private String name;

    public DishCategory() {
    }

    public DishCategory(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "DishCategory{" +
                "name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
