package ua.opu.kurs_gorbik_kozyrevych;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "dishImages")
@Data
public class DishImage {
    @Id
    private long id;
    private String imageString;
}
