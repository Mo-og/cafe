package ua.cafe.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.cafe.models.Detail;
import ua.cafe.models.ReadyStatus;
import ua.cafe.services.DetailService;
import ua.cafe.utils.ResponseFactory;

import javax.validation.Valid;
import java.util.List;

@Lazy
@Controller
@Slf4j
public class DetailsController {

    private static DetailService detailService;

    @Autowired
    public void setDetailService(DetailService service) {
        DetailsController.detailService = service;
    }

    //API
    //get
    @GetMapping("/api/detail")
    public ResponseEntity<String> getDetailById(@RequestParam Long id) {
        return detailService.getDetailResponse(id);
    }

    @GetMapping("/api/details/{orderId}")
    @ResponseBody
    public List<Detail> getDetailsOfOrder(@PathVariable Long orderId) {
        return detailService.getByOrderId(orderId);
    }

    //add
    @PostMapping("/api/detail")
    public ResponseEntity<?> apiAddDish(@RequestBody @Valid Detail detail, BindingResult result) {
        log.debug("Trying to save: " + detail.toString());
        ResponseEntity<?> ErrorsMap = ResponseFactory.createResponse(result);
        if (ErrorsMap != null) return ErrorsMap;
        Detail saved;
        try {
            saved = detailService.saveDetail(detail);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        }
        log.debug("Successfully saved: " + saved);
        return ResponseEntity.ok(saved);
    }

    /*@GetMapping("/api/detail")
    public ResponseEntity<String> apiViewOrderDetail(@RequestParam Long dish_id, @RequestParam Long order_id) {
        Detail detail = detailService.findByOrderIdAndDishID(order_id, dish_id);
        if (detail == null)
            return new ResponseEntity<>("No such order or dish in it", HttpStatus.NOT_FOUND);
        return JsonMaker.getJsonResponse(detail);
    }*/
    @PostMapping("/api/detail/status")
    public ResponseEntity<String> apiSetOrderDetailStatus(@RequestParam Long id, @RequestParam ReadyStatus status) {
        Detail detail = detailService.getById(id);
        if (detail == null)
            return new ResponseEntity<>("No detail found by given id", HttpStatus.NOT_FOUND);
        detail.setStatus(status);
        detailService.saveDetail(detail);
        return ResponseEntity.ok("Status was updated successfully");
    }

    //update | add dish to order
    @PutMapping(value = "/api/detail")
    public ResponseEntity<?> apiUpdateDetail(@RequestBody @Valid Detail detail, BindingResult result) {
        if (result.hasErrors())
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        return ResponseEntity.ok(detailService.saveDetail(detail));
    }

    //delete
    @DeleteMapping(value = "/api/detail")
    public ResponseEntity<String> apiRemoveDishFromOrder(@RequestParam Long id) {
        detailService.removeById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////


}
