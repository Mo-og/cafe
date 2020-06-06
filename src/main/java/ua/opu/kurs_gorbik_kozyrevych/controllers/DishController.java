package ua.opu.kurs_gorbik_kozyrevych.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.opu.kurs_gorbik_kozyrevych.Dish;
import ua.opu.kurs_gorbik_kozyrevych.services.DishService;
import ua.opu.kurs_gorbik_kozyrevych.services.WorkerService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.NoSuchElementException;

@Controller
public class DishController {

    private static DishService service;
    @Autowired
    WorkerService workerService;

    @Autowired
    public void setService(DishService service) {
        DishController.service = service;
    }

    public static List<Dish> getAllDishes() {
        return service.getAllDishes();
    }

    public static Dish getDishById(long id){return service.getById(id);}

    @GetMapping("/menu")
    public String getMenu(Model model, Principal principal ) {
        try{
            model.addAttribute("dishes", service.getAllDishes());
            final UserDetails user = workerService.loadUserByUsername(principal.getName());

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
        Dish dish = service.getById(id);
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
        service.saveDish(dish);
        System.out.println("Обновлено " + dish);
        return "redirect:/menu";
    }

    @PostMapping("/add_dish")
    public String greetingSubmit(@Valid Dish dish, BindingResult result, Model model) {
        if (result.hasErrors()){
            model.addAttribute("categories",CategoriesController.getAllCategories());
            return "Director/add_dish";
        }
        service.saveDish(dish);
        return "redirect:/menu";
    }

    @GetMapping("/dish_remove")
    public String removeDish(@RequestParam Long id) {
        if (!service.existsWithId(id))
            throw new NoSuchElementException();
        service.removeById(id);
        return "redirect:/menu";
    }
}
