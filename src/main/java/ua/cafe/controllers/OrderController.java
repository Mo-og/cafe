package ua.cafe.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.cafe.models.Order;
import ua.cafe.services.DetailService;
import ua.cafe.services.DishService;
import ua.cafe.services.OrderService;
import ua.cafe.services.PdfService;
import ua.cafe.utils.JsonMaker;
import ua.cafe.utils.ResponseFactory;
import ua.cafe.utils.Stats;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

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
    @GetMapping("/api/orders")
    @ResponseBody
    public ResponseEntity<String> apiGetOrders(@RequestParam(required = false) String datetimeFrom, @RequestParam(required = false) String datetimeTo) {
        List<Order> orders = orderService.getOrdersForDate(new Stats.Interval(datetimeFrom, datetimeTo));
        return JsonMaker.getJsonResponse(orders);
    }

    @GetMapping("/api/order")
    public ResponseEntity<String> apiGetOrder(@RequestParam Long id) {
        Order order = orderService.getById(id);
        if (order == null)
            return new ResponseEntity<>("No order found by given id", HttpStatus.NOT_FOUND);
        return JsonMaker.getJsonResponse(order);
    }

    @PostMapping("/api/order")
    public ResponseEntity<?> apiSaveOrder(@RequestBody @Valid Order order, BindingResult result) {
        log.info("**************************************POSTED ORDER:\n" + order);
        var ErrorsMap = ResponseFactory.createResponse(result);
        if (ErrorsMap != null) return ErrorsMap;

        var temp = orderService.saveNewOrder(order);
        log.info("Saved order: " + temp);
        return ResponseEntity.ok(temp);
    }

    @PutMapping("/api/order")
    public ResponseEntity<?> apiUpdateOrder(@RequestBody @Valid Order order, BindingResult result) {
        ResponseEntity<?> ErrorsMap = ResponseFactory.createResponse(result);
        if (ErrorsMap != null) return ErrorsMap;
        log.info("Got order: " + order.toString());

        var temp = orderService.updateOrder(order);
        log.info("Updated order as: " + temp.toString());

        return ResponseEntity.ok(temp);
    }

    //DELETE
    @DeleteMapping("/api/order")
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
    public String getOrders(Model model, @RequestParam(required = false) String datetimeFrom, @RequestParam(required = false) String datetimeTo) {
        /*model.addAttribute("orders", orderService.getOrdersForDate(new Stats.Interval(datetimeFrom, datetimeTo)));
        model.addAttribute("new_order", new Order());
        markPage(model, "orders");
*/
        return "orders";
    }

    //отчет
    @GetMapping("/report")
    public String getReport(
            Model model,
            @RequestParam(name = "datetimeFrom", required = false) String from,
            @RequestParam(name = "datetimeTo", required = false) String to,
            @RequestParam(name = "sortByQuantity", required = false, defaultValue = "true") boolean sortByQuantity,
            @RequestParam(name = "includeZeros", required = false) boolean includeZeros
    ) {
        model.addAttribute("sortByQuantity", sortByQuantity);
        model.addAttribute("includeZeros", includeZeros);
        Stats.Interval interval = new Stats.Interval(from, to);
        Order order = orderService.getReportOrder(interval, sortByQuantity, includeZeros);
        model.addAttribute("datetimeFrom", interval.getFromAsHtmlString());
        model.addAttribute("datetimeTo", interval.getToAsHtmlString());
        model.addAttribute("order", order);
        model.addAttribute("cost", Stats.formatNumber(order.getCost()));
        markPage(model, "report");
        return "report";
    }

    @PostMapping(value = "/report", consumes = "application/json", produces = "application/pdf")
    @ResponseBody
    public String getPdfReport(@RequestBody Map<String, String> params) {
        Stats.Interval interval = new Stats.Interval(params.get("datetimeFrom"), params.get("datetimeTo"));
        boolean sortByQuantity = Boolean.parseBoolean(params.get("sortByQuantity")),
                includeZeros = Boolean.parseBoolean(params.get("includeZeros")),
                override = Boolean.parseBoolean(params.get("override"));

        return pdfService.getPdf(interval, sortByQuantity, includeZeros, override);
    }

    @GetMapping("/change_status")
    public String editStatus(Model model, @RequestParam Long id) {
        Order order = orderService.getById(id);
        model.addAttribute("order", order);
        model.addAttribute("dishes", dishService.getAllDishes());
        return "change_status";
    }

}
