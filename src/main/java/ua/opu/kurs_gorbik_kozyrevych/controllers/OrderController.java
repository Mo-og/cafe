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
import ua.opu.kurs_gorbik_kozyrevych.services.OrderService;

import javax.validation.Valid;
import java.util.Date;
import java.util.NoSuchElementException;

@Controller
public class OrderController {

    public static OrderService service;


    @Autowired
    public void setService(OrderService service) {
        OrderController.service = service;
    }


    @GetMapping("/Waiter/orders")
    public String getOrders(Model model) {
        model.addAttribute("orders", service.getAllOrders());
        model.addAttribute("new_order", new Order());
        return "Waiter/orders";
    }


    @PostMapping("/Waiter/orders")
    public String saveOrder(@Valid Order order, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("orders", service.getAllOrders());
            model.addAttribute("new_order", new Order());
            return "Waiter/orders";
        }
        if (order.getDate_ordered() == null) {
            order.setDate_ordered(new Date(System.currentTimeMillis()));
        }
        service.saveOrder(order);
        return "redirect:/Waiter/orders";
    }

    @PostMapping("/Waiter/order_update")
    public String updateOrder(@Valid Order order, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("order", order);
            model.addAttribute("container", new Details());
            model.addAttribute("dishes", DishController.getAllDishes());
            return "Waiter/edit_order";
        }
        Order to_save = service.getById(order.getId());
        to_save.setTable_num(order.getTable_num());
        to_save.setComments(order.getComments());
        service.saveOrder(to_save);
        model.addAttribute("order", order);
        model.addAttribute("container", new Details());
        model.addAttribute("dishes", DishController.getAllDishes());
        return "redirect:/Waiter/order_edit?id=" + order.getId();
    }

    @GetMapping("/Waiter/order_edit")
    public String editDish(Model model, @RequestParam Long id) {
        Order order = service.getById(id);
        Details container = new Details();
//????        container.setDish_id(id);
        model.addAttribute("order", order);
        model.addAttribute("container", container);
        model.addAttribute("dishes", DishController.getAllDishes());
        return "Waiter/edit_order";
    }

    @GetMapping("/Waiter/order_remove")
    public String removeOrder(Model model, @RequestParam Long id) {
        model.addAttribute("orders", service.getAllOrders());
        model.addAttribute("new_order", new Order());
        if (!service.existsWithId(id))
            throw new NoSuchElementException();
        service.removeById(id);
        return "redirect:/Waiter/orders";
    }



    @GetMapping("/Waiter/order_dish_edit")
    public String editDishFromOrder(Model model, @RequestParam Long dish_id, @RequestParam Long order_id) {
        Order order = service.getById(order_id);
        Dish dish = order.getDetailsIfPresent(dish_id).getDish();
        if (dish == null)
            throw new NoSuchElementException();
        model.addAttribute("order", order);
        model.addAttribute("dish", dish);
        model.addAttribute("dishes", DishController.getAllDishes());
        return "redirect:/Waiter/edit_order_dish";
    }


}
