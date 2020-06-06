package ua.opu.kurs_gorbik_kozyrevych.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
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
import ua.opu.kurs_gorbik_kozyrevych.services.WorkerService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Date;
import java.util.NoSuchElementException;

@Controller
public class OrderController {

    public static OrderService service;
    @Autowired
    WorkerService workerService;


    @Autowired
    public void setService(OrderService service) {
        OrderController.service = service;
    }


    @GetMapping("/orders")
    public String getOrders(Model model, Principal principal) {
        try{
            model.addAttribute("orders", service.getAllOrders());
            model.addAttribute("new_order", new Order());

            final UserDetails user = workerService.loadUserByUsername(principal.getName());

            switch(user.getAuthorities().toString()) {
                case "[ROLE_WAITER]":return "Waiter/orders";
                case "[ROLE_ADMIN]":return "Director/orders";
                case "[ROLE_COOK]":  return "Cook/orders";
            }
        }
        catch (NullPointerException e) {
            return  "User/index";
        }
        return  "User/index";
    }


    @PostMapping("/orders")
    public String saveOrder(@Valid Order order, BindingResult result, Model model, Principal principal ) {
        try {
            final UserDetails user = workerService.loadUserByUsername(principal.getName());
            if (result.hasErrors()) {
                model.addAttribute("orders", service.getAllOrders());
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
            service.saveOrder(order);

            return "redirect:/orders";
        }
        catch(NullPointerException e) {
            return  "User/index";
        }
    }


    @PostMapping("/order_update")
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
        return "redirect:/order_edit?id=" + order.getId();
    }

    @GetMapping("/order_edit")
    public String editDish(Model model, @RequestParam Long id) {
        Order order = service.getById(id);
        Details container = new Details();
        model.addAttribute("order", order);
        model.addAttribute("container", container);
        model.addAttribute("dishes", DishController.getAllDishes());
        return "Waiter/edit_order";
    }

    @GetMapping("/report")
    public String getReport(Model model) {
        Order order = new Order();
        for (Order o : service.getAllOrders()) {
            for (Details d : o.getDetails()) {
                order.techAddDetail(d);
            }
        }
        model.addAttribute("order", order);
        return "Director/report";
    }

    @GetMapping("/change_status")
    public String editStatus(Model model, @RequestParam Long id) {
        Order order = service.getById(id);
        model.addAttribute("order", order);
        model.addAttribute("dishes", DishController.getAllDishes());
        return "Cook/change_status";
    }

    @GetMapping("/order_remove")
    public String removeOrder(Model model, @RequestParam Long id) {
        model.addAttribute("orders", service.getAllOrders());
        model.addAttribute("new_order", new Order());
        if (!service.existsWithId(id))
            throw new NoSuchElementException();
        service.removeById(id);
        return "redirect:/orders";
    }

}
