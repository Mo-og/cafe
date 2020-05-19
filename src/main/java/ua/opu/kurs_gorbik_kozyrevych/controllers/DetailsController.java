package ua.opu.kurs_gorbik_kozyrevych.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.opu.kurs_gorbik_kozyrevych.Details;
import ua.opu.kurs_gorbik_kozyrevych.Dish;
import ua.opu.kurs_gorbik_kozyrevych.Order;
import ua.opu.kurs_gorbik_kozyrevych.services.DetailsService;

@Controller
public class DetailsController {

    private DetailsService service;

    @Autowired
    public void setDetailsService(DetailsService service) {
        this.service = service;
    }

    @GetMapping("/Waiter/dish_exclude")
    public String removeDishFromOrder(@RequestParam Long dish_id, @RequestParam Long order_id) {
        service.removeByOrderIdAndDishID(order_id, dish_id);
        return "redirect:/Waiter/order_edit";
    }

    @PostMapping("/Waiter/order_dish_add")
    public String addDishToOrder(Details container, BindingResult result, Model model) {
        System.out.println("ПОЛУЧЕНО container: " + container);
        Order order = OrderController.service.getById(container.getOrder_id());
        Dish dish_toSave = DishController.getDishById(container.getDish_id());
        container.setDish_id(dish_toSave.getId());
        container.setDish(dish_toSave);
        container.setOrder(order);
        if (result.hasErrors()) {
            model.addAttribute("dish", dish_toSave);
            model.addAttribute("order", order);
            model.addAttribute("dishes", DishController.getAllDishes());
            model.addAttribute("container", container);
            return "Waiter/edit_order_dish";
        }
//        order.addDetail(container);
//        dish_toSave.addDetail(container);
//        System.out.println("order = " + order);
//        System.out.println("dish_toSave = " + dish_toSave);
//        System.out.println("container = "+ container);
//        //TODO: в этом месте ломается сохранение от order_id=null, хотя оно не null
//        service.saveOrder(order);
        service.saveDetail(container);
        model.addAttribute("order", order);
        model.addAttribute("dish", dish_toSave);
        model.addAttribute("dishes", DishController.getAllDishes());
        model.addAttribute("container", container);
        return "redirect:/Waiter/order_edit?id=" + container.getOrder_id();
    }
}
