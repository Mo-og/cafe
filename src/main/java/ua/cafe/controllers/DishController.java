package ua.cafe.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.cafe.entities.Dish;
import ua.cafe.services.DishService;
import ua.cafe.services.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.NoSuchElementException;

@Controller
public class DishController {

    private static DishService dishService;
    private static UserService userService;

    @Autowired
    public void setService(DishService service) {
        DishController.dishService = service;
    }
    @Autowired
    public void setService(UserService service) {
        userService = service;
    }

    public static List<Dish> getAllDishes() {
        return dishService.getAllDishes();
    }

    public static Dish getDishById(long id){return dishService.getById(id);}

    @GetMapping("/menu")
    public String getMenu(Model model, Principal principal ) {
        try{
            model.addAttribute("dishes", dishService.getAllDishes());
            final UserDetails user = userService.loadUserByUsername(principal.getName());

            switch(user.getAuthorities().toString()) {
                case "[ROLE_WAITER]":  return "Waiter/menu";
                case "[ROLE_COOK]":  return "Cook/menu";
                case "[ROLE_ADMIN]":  return "Director/menu";
            }
        }
        catch (NullPointerException e) {
            return  "User/menu";
        }
        return "User/menu";
    }


    @GetMapping("/add_dish")
    public String addDish(Model model) {
        model.addAttribute("dish", new Dish());
        model.addAttribute("categories",CategoriesController.getAllCategories());
        return "Director/add_dish";
    }

    @GetMapping("/dish_edit")
    public String editDish(Model model, @RequestParam Long id) {
        Dish dish = dishService.getById(id);
        model.addAttribute("dish", dish);
        model.addAttribute("categories",CategoriesController.getAllCategories());
        System.out.println("Получено " + dish);
        return "Director/edit_dish";
    }

    @PostMapping("/dish_update")
    public String updateDish(@Valid Dish dish, BindingResult result, Model model) {
        if (result.hasErrors()){
            model.addAttribute("categories",CategoriesController.getAllCategories());
            return "Director/edit_dish";
        }
        dishService.saveDish(dish);
        System.out.println("Обновлено " + dish);
        return "redirect:/menu";
    }

    @PostMapping("/add_dish")
    public String greetingSubmit(@Valid Dish dish, BindingResult result, Model model) {
        if (result.hasErrors()){
            model.addAttribute("categories",CategoriesController.getAllCategories());
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
