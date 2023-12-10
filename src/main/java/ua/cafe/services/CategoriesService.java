package ua.cafe.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import ua.cafe.models.Category;
import ua.cafe.models.Dish;
import ua.cafe.repositories.CategoriesRepository;

@Service
public class CategoriesService {

    private CategoriesRepository repository;

  @Autowired(required = false)
  public void setRepository(@Lazy CategoriesRepository repository) {
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
