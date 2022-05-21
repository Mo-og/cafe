package ua.cafe.services;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ua.cafe.models.Detail;
import ua.cafe.repositories.DetailRepository;
import ua.cafe.utils.JsonMaker;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class DetailService {

    private DetailRepository repository;

    @Autowired
    public void setRepository(DetailRepository repository) {
        this.repository = repository;
    }

    public Detail saveDetail(Detail detail) throws IllegalStateException {
        detail.persist();
        return repository.save(detail);
    }

    public void saveAll(List<Detail> details) {
        repository.saveAll(details);
    }

    public void forceSaveDetail(Detail detail) {
        repository.save(detail);
    }

    public Detail findByOrderIdAndDishID(long order_id, long dish_id) {
        return repository.findByOrder_idAndDish_id(order_id, dish_id);
    }

    //Deprecated
    public void removeByOrderIdAndDishID(long order_id, long dish_id) {
        Detail detail = repository.findByOrder_idAndDish_id(order_id, dish_id);
        detail.setDish(null);
        detail.setOrder(null);
        repository.delete(detail);
    }

    public void remove(Detail detail) {
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

    public ResponseEntity<String> getDetailResponse(Long id) {
        Optional<Detail> optionalDetail = repository.findById(id);
        if (optionalDetail.isEmpty())
            return new ResponseEntity<>("No detail found for given id.", HttpStatus.NOT_FOUND);
        return JsonMaker.getJsonResponse(optionalDetail.get());
    }
}