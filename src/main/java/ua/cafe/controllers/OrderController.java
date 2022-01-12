package ua.cafe.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.cafe.entities.*;
import ua.cafe.services.DetailService;
import ua.cafe.services.DishService;
import ua.cafe.services.OrderService;
import ua.cafe.services.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@Lazy
@Controller
public class OrderController {

    //Services setting
    public static OrderService orderService;
    public static DetailService detailService;
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

    @Autowired
    public void setService(DetailService service) {
        detailService = service;
    }

    //API
    @RequestMapping(value = "/api/orders", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> apiGetOrders() {
        List<Order> orders = orderService.getAllOrders();
        return JsonMaker.getJsonResponse(orders);
    }

    @GetMapping("/api/order")
    public ResponseEntity<String> apiGetOrder(@RequestParam Long id) {
        Order order = orderService.getById(id);
        if (order == null)
            return new ResponseEntity<>("No order found by given id", HttpStatus.NOT_FOUND);
        return JsonMaker.getJsonResponse(order);
    }

    //add
    @PostMapping("/api/order")
    public ResponseEntity<String> apiSaveOrder(@RequestBody @Valid Order order, BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>("Invalid order: " + result.getFieldErrors(), HttpStatus.NOT_ACCEPTABLE);
        }
        if (order.getDateOrdered() == null) {
            order.setDateOrdered(new Date(System.currentTimeMillis()));
        }
        orderService.saveOrder(order);
        return ResponseEntity.ok("Order was saved!");
    }

    //update
    @RequestMapping(value = "/api/order", method = RequestMethod.PUT)
    public ResponseEntity<String> apiUpdateOrder(@RequestBody @Valid Order order, BindingResult result) {
        if (result.hasErrors())
            return new ResponseEntity<>("Invalid order: " + result.getFieldErrors(), HttpStatus.NOT_ACCEPTABLE);
        System.out.println(order);
        order.getDetails().forEach(detail -> detail.setOrder(order));
        System.out.println(order.getDetails());
        orderService.saveOrder(order);
        return ResponseEntity.ok("Order was updated!");
    }

    //remove
    @RequestMapping(value = "/api/order", method = RequestMethod.DELETE)
    public ResponseEntity<String> apiRemoveOrder(@RequestParam Long id, Principal principal) {
        Role role = new Role(principal);
        if (!role.isAuthorised())
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        orderService.removeById(id);
        System.out.println("Removed order with id " + id);
        return ResponseEntity.ok("Removed order " + id);
    }

    ///////////////////////////////////////////////////////////
    @GetMapping("/orders")
    public String getOrders(Model model, Authentication authentication) {
        if (authentication == null) return "redirect:/";

        model.addAttribute("orders", orderService.getAllOrders());
        model.addAttribute("new_order", new Order());

        return switch (((User) authentication.getPrincipal()).getPosition()) {
            case WAITER -> "Waiter/orders";
            case COOK -> "Cook/orders";
            case DIRECTOR -> "Director/orders";
        };
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
            if (order.getDateOrdered() == null) {
                order.setDateOrdered(new Date(System.currentTimeMillis()));
            }
            orderService.saveOrder(order);

            //noinspection SpringMVCViewInspection
            return "redirect:/orders";
        } catch (NullPointerException e) {
            return "index";
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
        to_save.setTableNum(order.getTableNum());
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
        if (!orderService.existsWithId(id))
            throw new NoSuchElementException();
        model.addAttribute("orders", orderService.getAllOrders());
        model.addAttribute("new_order", new Order());
        orderService.removeById(id);
        //noinspection SpringMVCViewInspection
        return "redirect:/orders";
    }

}
