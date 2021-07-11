package ua.cafe.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.cafe.entities.Detail;
import ua.cafe.services.DishService;
import ua.cafe.entities.Dish;
import ua.cafe.entities.Order;
import ua.cafe.services.DetailsService;

@Controller
public class DetailsController {

    private static DetailsService detailsService;
    private static DishService dishService;

    @Autowired
    public void setService(DishService service) {
        dishService = service;
    }

    public static void removeDishFromOrder(long dish_id, long order_id) {
        detailsService.removeByOrderIdAndDishID(order_id, dish_id);
    }

    @Autowired
    public void setDetailsService(DetailsService service) {
        DetailsController.detailsService = service;
    }

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
