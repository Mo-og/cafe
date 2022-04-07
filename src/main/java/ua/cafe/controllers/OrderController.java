package ua.cafe.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.cafe.models.Detail;
import ua.cafe.models.Dish;
import ua.cafe.models.Order;
import ua.cafe.services.DetailService;
import ua.cafe.services.DishService;
import ua.cafe.services.OrderService;
import ua.cafe.services.PdfService;
import ua.cafe.utils.JsonMaker;
import ua.cafe.utils.ResponseFactory;

import javax.validation.Valid;
import java.util.List;

import static ua.cafe.utils.Stats.markPage;

@Lazy
@Controller
@Slf4j
public class OrderController {

    //Services setting
    public static OrderService orderService;
    public static DetailService detailService;
    private static DishService dishService;
    private static PdfService pdfService;

    @Autowired
    public void setService(DishService service) {
        dishService = service;
    }

    @Autowired
    public void setService(OrderService service) {
        orderService = service;
    }

    @Autowired
    public void setService(DetailService service) {
        detailService = service;
    }

    @Autowired
    public void setService(PdfService service) {
        pdfService = service;
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

    //POST
    @PostMapping("/api/order")
    public ResponseEntity<?> apiSaveOrder(@RequestBody @Valid Order order, BindingResult result) {
        var ErrorsMap = ResponseFactory.createResponse(result);
        if (ErrorsMap != null) return ErrorsMap;

        var temp = orderService.saveNewOrder(order);
        log.info("Saved order: " + temp);
        return ResponseEntity.ok(temp);
    }

    //PUT
    @RequestMapping(value = "/api/order", method = RequestMethod.PUT)
    public ResponseEntity<?> apiUpdateOrder(@RequestBody @Valid Order order, BindingResult result) {
        ResponseEntity<?> ErrorsMap = ResponseFactory.createResponse(result);
        if (ErrorsMap != null) return ErrorsMap;
        log.info("Got order: " + order.toString());

        var temp = orderService.updateOrder(order);
        log.info("Updated order as: " + temp.toString());

        return ResponseEntity.ok(temp);
    }

    //DELETE
    @RequestMapping(value = "/api/order", method = RequestMethod.DELETE)
    public ResponseEntity<String> apiRemoveOrder(@RequestParam Long id) {
        try {
            orderService.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            return new ResponseEntity<>("No order found with id " + id, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok("Removed order " + id);
    }

    ///////////////////////////////////////////////////////////
    @GetMapping("/orders")
    public String getOrders(Model model, Authentication authentication) {
        if (authentication == null) return "redirect:/";

        model.addAttribute("orders", orderService.getAllOrders());
        model.addAttribute("new_order", new Order());
        markPage(model, "orders");

        return "orders";
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
        markPage(model, "report");

        return "report";
    }

    @GetMapping("/change_status")
    public String editStatus(Model model, @RequestParam Long id) {
        Order order = orderService.getById(id);
        model.addAttribute("order", order);
        model.addAttribute("dishes", dishService.getAllDishes());
        return "change_status";
    }

}
