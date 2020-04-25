package ua.opu.kurs_gorbik_kozyrevych;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ua.opu.kurs_gorbik_kozyrevych.services.CategoriesService;

@SpringBootApplication
public class KursGorbikKozyrevychApplication {
    private static CategoriesService service;

    public static void main(String[] args) {
        SpringApplication.run(KursGorbikKozyrevychApplication.class, args);
//        service.saveCategory(new DishCategory("Дессерт", (long) 1));
//        service.saveCategory(new DishCategory("Горячий напиток", (long) 2));
    }

}
