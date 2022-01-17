package ua.cafe.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.cafe.entities.DishCategory;
import ua.cafe.repositories.CategoriesRepository;

import java.util.List;

@Service
public class CategoriesService {

    private CategoriesRepository repository;

    @Autowired
    public void setRepository(CategoriesRepository repository) {
        this.repository = repository;
    }

    public DishCategory saveCategory(DishCategory dishCategory) {
        return repository.save(dishCategory);
    }

    public List<DishCategory> getAllCategories() {
        return repository.findAllByOrderByName();
    }

    public void deleteById(long id) {
        repository.deleteById(id);
    }

    public boolean existsWithId(long id) {
        return repository.existsById(id);
    }

    public DishCategory getById(long id) {
        return repository.getOne(id);
    }

}
