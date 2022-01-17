package ua.cafe.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.cafe.entities.Detail;
import ua.cafe.entities.Dish;
import ua.cafe.entities.Order;
import ua.cafe.services.DetailService;
import ua.cafe.services.DishService;
import ua.cafe.utils.JsonMaker;
import ua.cafe.utils.Role;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Lazy
@Controller
public class DetailsController {

    private static DetailService detailService;
    private static DishService dishService;

    @Autowired
    public void setService(DishService service) {
        dishService = service;
    }

    @Autowired
    public void setDetailsService(DetailService service) {
        DetailsController.detailService = service;
    }

    //API
    //get
    @GetMapping("/api/detail")
    public ResponseEntity<String> apiViewOrderDetail(@RequestParam Long dish_id, @RequestParam Long order_id, HttpServletRequest httpServletRequest) {
        if (new Role(httpServletRequest).isAuthorised())
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        Detail detail = detailService.findByOrderIdAndDishID(order_id, dish_id);
        if (detail == null)
            return new ResponseEntity<>("No such order or dish in it", HttpStatus.NOT_FOUND);
        return JsonMaker.getJsonResponse(detail);
    }

    //add
    @PostMapping("/api/detail")
    public ResponseEntity<String> apiAddDishToOrder(@Valid Detail detail, BindingResult result, HttpServletRequest httpServletRequest) {
        if (new Role(httpServletRequest).isAuthorised())
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        if (result.hasErrors())
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        detailService.saveDetail(detail);
        return ResponseEntity.ok("Detail was saved successfully");
    }

    //update | add dish to order
    @RequestMapping(value = "/api/detail", method = RequestMethod.PUT)
    public ResponseEntity<String> apiUpdateDetail(@Valid Detail detail, BindingResult result) {
        if (result.hasErrors())
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
//TODO: simplify or optimise
        Order order = OrderController.orderService.getById(detail.getOrderId());
        Dish dish_toSave = dishService.getById(detail.getDishId());
        detailService.remove(detail);
        detail.setDish(dish_toSave);
        detail.setOrder(order);
        detailService.saveDetail(detail);
        return ResponseEntity.ok("Detail was updated successfully");
    }

    //delete
    @RequestMapping(value = "/api/detail", method = RequestMethod.DELETE)
    public ResponseEntity<String> apiRemoveDishFromOrder(@RequestParam Long dish_id, @RequestParam Long order_id) {
        detailService.removeByOrderIdAndDishID(order_id, dish_id);
        return ResponseEntity.ok("Dish was removed from order if existed!");
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////


}
