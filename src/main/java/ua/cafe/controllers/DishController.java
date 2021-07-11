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
import ua.cafe.entities.Dish;
import ua.cafe.entities.JsonMaker;
import ua.cafe.services.CategoriesService;
import ua.cafe.services.DishService;
import ua.cafe.services.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.NoSuchElementException;

@Controller
public class DishController {

    //Services assignment
    private static DishService dishService;
    private static UserService userService;
    private static CategoriesService categoriesService;

    @Autowired
    public void setService(DishService service) {
        DishController.dishService = service;
    }

    @Autowired
    public void setService(UserService service) {
        userService = service;
    }

    @Autowired
    public void setService(CategoriesService service) {
        categoriesService = service;
    }

    @GetMapping("/menuJSON")
    public ResponseEntity<String> getMenuJson() {
        String json;
        List<Dish> dishes = dishService.getAllDishes();
        for (Dish dish : dishes) {
            dish.setDetails(null);
        }
        return JsonMaker.getJsonResponse(dishes);
    }

    @GetMapping("/dishJSON")
    public ResponseEntity<String> getDishById(@RequestParam Long id) {
        return JsonMaker.getJsonResponse(dishService.getById(id));
    }

    @PostMapping("/dish_update")
    public ResponseEntity<String> updateDish(@Valid Dish dish, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        }
        dishService.saveDish(dish);
        System.out.println("Dish updated: " + dish);
        return ResponseEntity.ok("Dish updated!");
    }

    @GetMapping("/menu")
    public String getMenu(Principal principal) {
        try {
            final UserDetails user = userService.loadUserByUsername(principal.getName());
            switch (user.getAuthorities().toString()) {
                case "[ROLE_WAITER]":
                    return "Waiter/menu";
                case "[ROLE_COOK]":
                    return "Cook/menu";
                case "[ROLE_ADMIN]":
                    return "Director/menu";
            }
        } catch (NullPointerException e) {
            return "User/menu";
        }
        return "User/menu";
    }
////////////////////////
    @GetMapping("/add_dish")
    public String addDish(Model model) {
        model.addAttribute("dish", new Dish());
        model.addAttribute("categories", categoriesService.getAllCategories());
        return "Director/add_dish";
    }

    @GetMapping("/dish_edit")
    public String editDish(Model model, @RequestParam Long id) {
        Dish dish = dishService.getById(id);
        model.addAttribute("dish", dish);
        model.addAttribute("categories", categoriesService.getAllCategories());
        System.out.println("Получено " + dish);
        return "Director/edit_dish";
    }

    @PostMapping("/dish_update1")
    public String updateDish1(@Valid Dish dish, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoriesService.getAllCategories());
            return "Director/edit_dish";
        }
        dishService.saveDish(dish);
        System.out.println("Обновлено " + dish);
        return "redirect:/menu";
    }

    @PostMapping("/add_dish")
    public String greetingSubmit(@Valid Dish dish, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoriesService.getAllCategories());
            return "Director/add_dish";
        }
        dishService.saveDish(dish);
        return "redirect:/menu";
    }

    @GetMapping("/dish_remove")
    public String removeDish(@RequestParam Long id) {
        if (!dishService.existsWithId(id))
            throw new NoSuchElementException();
        dishService.removeById(id);
        return "redirect:/menu";
    }
}
