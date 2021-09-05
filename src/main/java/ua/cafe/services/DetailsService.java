package ua.cafe.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import ua.cafe.entities.Detail;
import ua.cafe.repositories.DetailsOfOrderedDishRepository;

import java.util.List;

@Lazy
@Service
public class DetailsService {

    private DetailsOfOrderedDishRepository repository;

    @Autowired
    public void setRepository(DetailsOfOrderedDishRepository repository) {
        this.repository = repository;
    }

    public void saveDetail(Detail detail) {
        Detail det = repository.findByOrder_idAndDish_id(detail.getOrder_id(), detail.getDish_id());
        if (det != null) {
            det.setQuantity(det.getQuantity() + detail.getQuantity());
            repository.save(det);
            return;
        }
        repository.save(detail);
    }

    public void forceSaveDetail(Detail detail) {
        repository.save(detail);
    }

    public Detail findByOrderIdAndDishID(long order_id, long dish_id) {
        return repository.findByOrder_idAndDish_id(order_id, dish_id);
    }

    public void removeByOrderIdAndDishID(long order_id, long dish_id) {
        Detail detail = repository.findByOrder_idAndDish_id(order_id, dish_id);
        detail.setDish(null);
        detail.setOrder(null);
        repository.delete(detail);
    }

    public void remove(Detail detail) {
        detail.setDish(null);
        detail.setOrder(null);
        repository.delete(detail);
    }

    public List<Detail> getAllDishes() {
        return repository.findAll();
    }

    public void removeById(long id) {
        repository.deleteById(id);
    }

    public boolean existsWithId(long id) {
        return repository.existsById(id);
    }

    public Detail getById(long id) {
        return repository.getOne(id);
    }

}