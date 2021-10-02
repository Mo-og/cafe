package ua.cafe.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.cafe.entities.*;
import ua.cafe.services.DishService;
import ua.cafe.services.DetailsService;

import javax.validation.Valid;
import java.security.Principal;

@Lazy
@Controller
public class DetailsController {

    private static DetailsService detailsService;
    private static DishService dishService;

    @Autowired
    public void setService(DishService service) {
        dishService = service;
    }

    @Autowired
    public void setDetailsService(DetailsService service) {
        DetailsController.detailsService = service;
    }

    //API
    //get
    @GetMapping("/api/detail")
    public ResponseEntity<String> apiViewOrderDetail(@RequestParam Long dish_id, @RequestParam Long order_id, Principal principal) {
        if (!Role.isAuthorized(principal))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        Detail detail = detailsService.findByOrderIdAndDishID(order_id, dish_id);
        if (detail == null)
            return new ResponseEntity<>("No such order or dish in it", HttpStatus.NOT_FOUND);
        return JsonMaker.getJsonResponse(detail);
    }

    //add
    @PostMapping("/api/detail")
    public ResponseEntity<String> apiAddDishToOrder(@Valid Detail detail, BindingResult result, Principal principal) {
        /*if (!Role.isAuthorized(principal))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);*/
        if (result.hasErrors())
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        detailsService.saveDetail(detail);
        return ResponseEntity.ok("Detail was saved successfully");
    }

    //update | add dish to order
    @RequestMapping(value = "/api/detail", method = RequestMethod.PUT)
    public ResponseEntity<String> apiUpdateDetail(@Valid Detail detail, BindingResult result) {
        if (result.hasErrors())
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
//TODO: simplify or optimise
        Order order = OrderController.orderService.getById(detail.getOrder_id());
        Dish dish_toSave = dishService.getById(detail.getDish_id());
        detailsService.remove(detail);
        detail.setDish(dish_toSave);
        detail.setOrder(order);
        detailsService.saveDetail(detail);
        return ResponseEntity.ok("Detail was updated successfully");
    }

    //delete
    @RequestMapping(value = "/api/detail", method = RequestMethod.DELETE)
    public ResponseEntity<String> apiRemoveDishFromOrder(@RequestParam Long dish_id, @RequestParam Long order_id, Principal principal) {
        /*if (!Role.isAuthorized(principal))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);*/
        detailsService.removeByOrderIdAndDishID(order_id, dish_id);
        return ResponseEntity.ok("Dish was removed from order!");
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @GetMapping("/dish_exclude")
    public String removeDishFromOrder(@RequestParam Long dish_id, @RequestParam Long order_id, Model model) {
        detailsService.removeByOrderIdAndDishID(order_id, dish_id);
        model.addAttribute("order", OrderController.orderService.getById(order_id));
        model.addAttribute("dishes", dishService.getAllDishes());
        model.addAttribute("container", new Detail());
        return "redirect:/order_edit?id=" + order_id;
    }

    @PostMapping("/order_dish_add")
    public String addDishToOrder(Detail container, BindingResult result, Model model) {
        System.out.println("ПОЛУЧЕНО container: " + container);
        Order order = OrderController.orderService.getById(container.getOrder_id());
        Dish dish_toSave = dishService.getById(container.getDish_id());
//        container.setDish_id(dish_toSave.getId());
        container.setDish(dish_toSave);
        container.setOrder(order);
        if (result.hasErrors()) {
            model.addAttribute("dish", dish_toSave);
            model.addAttribute("order", order);
            model.addAttribute("dishes", dishService.getAllDishes());
            model.addAttribute("container", container);
            return "Waiter/edit_order_dish";
        }
        detailsService.saveDetail(container);
        model.addAttribute("order", order);
        model.addAttribute("dish", dish_toSave);
        model.addAttribute("dishes", dishService.getAllDishes());
        model.addAttribute("container", container);
        return "redirect:/order_edit?id=" + container.getOrder_id();
    }

    @GetMapping("/order_dish_edit")
    public String viewDishFromOrder(Model model, @RequestParam Long dish_id, @RequestParam Long order_id) {
        Detail detail = detailsService.findByOrderIdAndDishID(order_id, dish_id);
        model.addAttribute("order", detail.getOrder());
        model.addAttribute("dish", detail.getDish());
        model.addAttribute("dishes", dishService.getAllDishes());
        model.addAttribute("container", detail);
        return "Waiter/edit_order_dish";
    }

    @PostMapping("/order_dish_edit")
    public String editDishFromOrder(@RequestParam Long dish_id, Detail container, BindingResult result, Model model) {
        System.out.println("ПОЛУЧЕНО container: " + container);
        Order order = OrderController.orderService.getById(container.getOrder_id());
        Dish dish_toSave = dishService.getById(container.getDish_id());
        if (result.hasErrors()) {
            model.addAttribute("order", order);
            model.addAttribute("dishes", dishService.getAllDishes());
            model.addAttribute("container", container);
            return "Waiter/edit_order_dish";
        }
        detailsService.remove(container);
        container.setDish(dish_toSave);
        container.setOrder(order);
        detailsService.saveDetail(container);
        model.addAttribute("order", order);
        model.addAttribute("dish", dish_toSave);
        model.addAttribute("dishes", dishService.getAllDishes());
        model.addAttribute("container", container);
        return "redirect:/order_edit?id=" + container.getOrder_id();
    }


}
