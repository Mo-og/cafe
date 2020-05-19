package ua.opu.kurs_gorbik_kozyrevych.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.opu.kurs_gorbik_kozyrevych.Dish;
import ua.opu.kurs_gorbik_kozyrevych.services.DishService;

import javax.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;

@Controller
public class DishController {

    private static DishService service;

    @Autowired
    public void setService(DishService service) {
        DishController.service = service;
    }

    public static List<Dish> getAllDishes() {
        return service.getAllDishes();
    }
    public static Dish getDishById(long id){return service.getById(id);}

    @GetMapping("/User/menu")
    public String getMenu(Model model) {
        model.addAttribute("dishes", service.getAllDishes());
        return "User/menu";
    }

    @GetMapping("/Director/menu")
    public String getMenuDirector(Model model) {
        model.addAttribute("dishes", service.getAllDishes());
        return "Director/menu";
    }

    @GetMapping("/Waiter/menu")
    public String getMenuWaiter(Model model) {
        model.addAttribute("dishes", service.getAllDishes());
        return "Waiter/menu";
    }


    @GetMapping("/Director/add_dish")
    public String addDish(Model model) {
        model.addAttribute("dish", new Dish());
        model.addAttribute("categories",CategoriesController.getAllCategories());
        return "Director/add_dish";
    }

    @GetMapping("/Director/dish_edit")
    public String editDish(Model model, @RequestParam Long id) {
        Dish dish = service.getById(id);
        model.addAttribute("dish", dish);
        model.addAttribute("categories",CategoriesController.getAllCategories());
        System.out.println("Получено " + dish);
        return "Director/edit_dish";
    }

    @PostMapping("/Director/dish_update")
    public String updateDish(@Valid Dish dish, BindingResult result, Model model) {
        if (result.hasErrors()){
            model.addAttribute("categories",CategoriesController.getAllCategories());
            return "Director/edit_dish";
        }
        service.saveDish(dish);
        System.out.println("Обновлено " + dish);
        return "redirect:/Director/menu";
    }

    @PostMapping("/Director/add_dish")
    public String greetingSubmit(@Valid Dish dish, BindingResult result, Model model) {
        if (result.hasErrors()){
            model.addAttribute("categories",CategoriesController.getAllCategories());
            return "Director/add_dish";
        }
        service.saveDish(dish);
        return "redirect:/Director/menu";
    }

    @GetMapping("/Director/dish_remove")
    public String removeDish(@RequestParam Long id) {
        if (!service.existsWithId(id))
            throw new NoSuchElementException();
        service.removeById(id);
        return "redirect:/Director/menu";
    }
}
