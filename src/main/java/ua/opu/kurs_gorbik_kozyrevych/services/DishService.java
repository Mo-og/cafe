package ua.opu.kurs_gorbik_kozyrevych.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.opu.kurs_gorbik_kozyrevych.Dish;
import ua.opu.kurs_gorbik_kozyrevych.repositories.DishRepository;


import java.util.Comparator;
import java.util.List;

@Service
public class DishService {

    private DishRepository repository;

    @Autowired
    public void setRepository(DishRepository repository) {
        this.repository = repository;
    }

    public void saveDish(Dish dish) {
        repository.save(dish);
    }

    public List<Dish> getAllDishes() {
        return repository.findAllByOrderByCategory();
    }

    public void removeById(long id) {
        repository.deleteById(id);
    }

    public boolean existsWithId(long id) {
        return repository.existsById(id);
    }

    public Dish getById(long id) {
        return repository.getOne(id);
    }

}
