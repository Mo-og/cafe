package ua.cafe.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.cafe.entities.Dish;
import ua.cafe.repositories.DishRepository;

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
        if (o1.getCategoryId() == o2.getCategoryId())
            return o1.getName().compareToIgnoreCase(o2.getName());
        else return (int) (o1.getCategoryId() - o2.getCategoryId());
    };

    public List<Dish> getAllDishes() {
        List<Dish> list = repository.findAll();
        list.sort(comparator);
        return list;
    }

    public void removeById(long id) {
        repository.deleteById(id);
    }

    public boolean isNoneWithId(long id) {
        return !repository.existsById(id);
    }

    public Dish getById(long id) {
        return repository.findById(id).orElse(null);
    }

}
