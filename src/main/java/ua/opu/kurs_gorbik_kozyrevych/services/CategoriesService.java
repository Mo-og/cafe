package ua.opu.kurs_gorbik_kozyrevych.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.opu.kurs_gorbik_kozyrevych.DishCategory;
import ua.opu.kurs_gorbik_kozyrevych.repositories.CategoriesRepository;

import java.util.List;

@Service
public class CategoriesService {

        private CategoriesRepository repository;

        @Autowired
        public void setRepository(CategoriesRepository repository) {
            this.repository = repository;
        }

        public void saveCategory(DishCategory dishCategory) {
            repository.save(dishCategory);
        }

        public List<DishCategory> getAllCategories() {
            return repository.findAll();
        }

        public void removeById(long id) {
            repository.deleteById(id);
        }

        public boolean existsWithId(long id) {
            return repository.existsById(id);
        }

        public DishCategory getById(long id) {
            return repository.getOne(id);
        }

}
