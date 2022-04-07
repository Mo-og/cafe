package ua.cafe.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.cafe.models.Category;
import ua.cafe.models.Dish;
import ua.cafe.repositories.CategoriesRepository;

import java.util.List;

@Service
public class CategoriesService {

    private CategoriesRepository repository;

    @Autowired
    public void setRepository(CategoriesRepository repository) {
        this.repository = repository;
    }

    public Category saveCategory(Category category) {
        return repository.save(category);
    }

    public List<Category> getAllCategories() {
        return repository.findAllByOrderByName();
    }

    public void deleteById(long id) {
        repository.deleteById(id);
    }

    public boolean existsWithId(long id) {
        return repository.existsById(id);
    }

    public Category getById(long id) {
        return repository.getOne(id);
    }

    public void setById(Dish dish, long categoryId) {
        dish.setCategory(repository.getById(categoryId));
    }
}
