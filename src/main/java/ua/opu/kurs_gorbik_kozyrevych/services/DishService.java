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

    Comparator<Dish> comparator = (o1, o2) -> {
        if (o1.getCategory().compareTo(o2.getCategory()) == 0) return o1.getName().compareToIgnoreCase(o2.getName());
        return 0;
    };

    public List<Dish> getAllDishes() {
        List<Dish> list = repository.findAllByOrderByCategory();
        list.sort(comparator);
        return list;
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
