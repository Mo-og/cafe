package ua.cafe.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.cafe.entities.*;
import ua.cafe.services.DishService;
import ua.cafe.services.OrderService;
import ua.cafe.services.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Controller
public class OrderController {

    public static OrderService orderService;
    private static DishService dishService;
    private static UserService userService;

    @Autowired
    public void setService(DishService service) {
        dishService = service;
    }

    @Autowired
    public void setService(UserService service) {
        userService = service;
    }

    @Autowired
    public void setService(OrderService service) {
        orderService = service;
    }

    @GetMapping("/ordersJSON")
    public ResponseEntity<String> getOrdersJSON(Principal principal) {
            final UserDetails user = userService.loadUserByUsername(principal.getName());
            if (!Role.isAuthorised(user.getAuthorities().toString()))
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return JsonMaker.getJsonResponse(orderService.getAllOrders());
    }

    @GetMapping("/orders")
    public String getOrders(Model model, Principal principal) {
        try {
            model.addAttribute("orders", orderService.getAllOrders());
            model.addAttribute("new_order", new Order());

            final UserDetails user = userService.loadUserByUsername(principal.getName());

            switch (user.getAuthorities().toString()) {
                case "[ROLE_WAITER]":
                    return "Waiter/orders";
                case "[ROLE_ADMIN]":
                    return "Director/orders";
                case "[ROLE_COOK]":
                    return "Cook/orders";
            }
        } catch (NullPointerException e) {
            return "User/index";
        }
        return "User/index";
    }


    @PostMapping("/orders")
    public String saveOrder(@Valid Order order, BindingResult result, Model model, Principal principal) {
        try {
            final UserDetails user = userService.loadUserByUsername(principal.getName());
            if (result.hasErrors()) {
                model.addAttribute("orders", orderService.getAllOrders());
                model.addAttribute("new_order", new Order());
                switch (user.getAuthorities().toString()) {
                    case "[ROLE_WAITER]":
                        return "Waiter/orders";
                    case "[ROLE_COOK]":
                        return "Cook/orders";
                }
            }
            if (order.getDate_ordered() == null) {
                order.setDate_ordered(new Date(System.currentTimeMillis()));
            }
            orderService.saveOrder(order);

            return "redirect:/orders";
        } catch (NullPointerException e) {
            return "User/index";
        }
    }


    @PostMapping("/order_update")
    public String updateOrder(@Valid Order order, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("order", order);
            model.addAttribute("container", new Detail());
            model.addAttribute("dishes", dishService.getAllDishes());
            return "Waiter/edit_order";
        }
        Order to_save = orderService.getById(order.getId());
        to_save.setTable_num(order.getTable_num());
        to_save.setComments(order.getComments());
        orderService.saveOrder(to_save);
        model.addAttribute("order", order);
        model.addAttribute("container", new Detail());
        model.addAttribute("dishes", dishService.getAllDishes());
        return "redirect:/order_edit?id=" + order.getId();
    }

    @GetMapping("/order_edit")
    public String editDish(Model model, @RequestParam Long id) {
        Order order = orderService.getById(id);
        Detail container = new Detail();
        model.addAttribute("order", order);
        model.addAttribute("container", container);
        model.addAttribute("dishes", dishService.getAllDishes());
        return "Waiter/edit_order";
    }

    //отчет
    @GetMapping("/report")
    public String getReport(Model model) {
        // Report is represented as an order of all dishes
        Order order = new Order();
        List<Dish> dishes = dishService.getAllDishes();
        for (Dish d : dishes) {
            // We need to add all existing dishes (with quantity of 0) to see which ones were not ordered,
            // otherwise we won't be able to tell which dishes had zero success
            order.addDetail(new Detail(d.getId(), -1, 0));
        }
        for (Order o : orderService.getAllOrders()) {
            for (Detail d : o.getDetails()) {
                order.addDetail(d);
            }
        }
        order.sortByQuantity();
        model.addAttribute("order", order);
        return "Director/report";
    }

    @GetMapping("/change_status")
    public String editStatus(Model model, @RequestParam Long id) {
        Order order = orderService.getById(id);
        model.addAttribute("order", order);
        model.addAttribute("dishes", dishService.getAllDishes());
        return "Cook/change_status";
    }

    @GetMapping("/order_remove")
    public String removeOrder(Model model, @RequestParam Long id) {
        model.addAttribute("orders", orderService.getAllOrders());
        model.addAttribute("new_order", new Order());
        if (!orderService.existsWithId(id))
            throw new NoSuchElementException();
        orderService.removeById(id);
        return "redirect:/orders";
    }

}
