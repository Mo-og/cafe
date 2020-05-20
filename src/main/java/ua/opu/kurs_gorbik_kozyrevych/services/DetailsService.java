package ua.opu.kurs_gorbik_kozyrevych.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.opu.kurs_gorbik_kozyrevych.Details;
import ua.opu.kurs_gorbik_kozyrevych.repositories.DetailsOfOrderedDishRepository;

import java.util.List;


@Service
public class DetailsService {

    private DetailsOfOrderedDishRepository repository;

    @Autowired
    public void setRepository(DetailsOfOrderedDishRepository repository) {
        this.repository = repository;
    }

    public void saveDetail(Details detail) {
        Details det = repository.findByOrder_idAndDish_id(detail.getOrder_id(), detail.getDish_id());
        if (det != null) {
            det.setQuantity(det.getQuantity() + detail.getQuantity());
            repository.save(det);
            return;
        }
        repository.save(detail);
    }

    public Details findByOrderIdAndDishID(long order_id, long dish_id) {
        return repository.findByOrder_idAndDish_id(order_id, dish_id);
    }

    public void removeByOrderIdAndDishID(long order_id, long dish_id) {
        Details detail = repository.findByOrder_idAndDish_id(order_id, dish_id);
        detail.setDish(null);
        detail.setOrder(null);
        repository.delete(detail);
    }

    public void remove(Details details) {
        repository.delete(details);
    }

    public List<Details> getAllDishes() {
        return repository.findAll();
    }

    public void removeById(long id) {
        repository.deleteById(id);
    }

    public boolean existsWithId(long id) {
        return repository.existsById(id);
    }

    public Details getById(long id) {
        return repository.getOne(id);
    }

}