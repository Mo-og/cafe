package ua.opu.kurs_gorbik_kozyrevych.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.opu.kurs_gorbik_kozyrevych.DishCategory;
import ua.opu.kurs_gorbik_kozyrevych.services.CategoriesService;

import javax.validation.Valid;
import java.util.List;

import java.util.NoSuchElementException;

@Controller
public class CategoriesController {

    private static CategoriesService service;

    public static List<DishCategory> getAllCategories() {
        return service.getAllCategories();
    }

    @Autowired
    public void setService(CategoriesService service) {
        CategoriesController.service = service;
    }

    @GetMapping("/Director/categories")
    public String getCategory(Model model) {
        model.addAttribute("categories", service.getAllCategories());
        return "Director/categories";
    }

    @GetMapping("/Waiter/categories")
    public String getCategoryWaiter(Model model) {
        model.addAttribute("categories", service.getAllCategories());
        return "Waiter/categories";
    }

    @GetMapping("/Director/add_category")
    public String addDishCategory(Model model) {
        model.addAttribute("category", new DishCategory());
        return "Director/add_category";
    }

    @GetMapping("/Director/category_edit")
    public String editDish(Model model, @RequestParam Long id) {
        DishCategory category = service.getById(id);
        model.addAttribute("category", category);
        System.out.println("Получено " + category);
        return "Director/edit_category";
    }

    @PostMapping("/Director/category_update")
    public String updateDish(@Valid DishCategory category, BindingResult result) {
        if (result.hasErrors())
            return "Director/edit_category";
        service.saveCategory(category);
        System.out.println("Обновлено " + category);
        return "redirect:/Director/categories";
    }

    @PostMapping("/Director/add_category")
    public String addDishCatPost(@Valid DishCategory category, BindingResult result) {
        System.out.println("Отправлено " + category);
        if (result.hasErrors())
            return "/Director/add_category";
        service.saveCategory(category);
        System.out.println("Успешно добавлено " + category);
        return "redirect:/Director/categories";
    }

    @GetMapping("/Director/category_remove")
    public String removeCategory(@RequestParam Long id) {
        if (!service.existsWithId(id))
            throw new NoSuchElementException();
        service.removeById(id);
        return "redirect:/Director/categories";
    }
}
