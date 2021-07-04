package ua.cafe.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.cafe.services.CategoriesService;
import ua.cafe.services.UserService;
import ua.cafe.entities.DishCategory;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.NoSuchElementException;

@Controller
public class CategoriesController {

    private static CategoriesService service;
    private static UserService userService;

    @Autowired
    public void setService(UserService service) {
        userService = service;
    }

    @Autowired
    public void setService(CategoriesService service) {
        CategoriesController.service = service;
    }

    public static List<DishCategory> getAllCategories() {
        return service.getAllCategories();
    }

    @GetMapping("/categories")
    public String getCategory(Model model, Principal principal) {

        try {
            final UserDetails user = userService.loadUserByUsername(principal.getName());
            model.addAttribute("categories", service.getAllCategories());
            switch (user.getAuthorities().toString()) {
                case "[ROLE_ADMIN]":
                    return "Director/categories";
                case "[ROLE_WAITER]":
                    return "Waiter/categories";
            }
        } catch (NullPointerException e) {
            return "User/index";
        }
        return "User/index";
    }

    @GetMapping("/add_category")
    public String addDishCategory(Model model) {
        model.addAttribute("category", new DishCategory());
        return "Director/add_category";
    }

    @GetMapping("/category_edit")
    public String editDish(Model model, @RequestParam Long id) {
        DishCategory category = service.getById(id);
        model.addAttribute("category", category);
        System.out.println("Получено " + category);
        return "Director/edit_category";
    }

    @PostMapping("/category_update")
    public String updateDish(@Valid DishCategory category, BindingResult result) {
        if (result.hasErrors())
            return "Director/edit_category";
        service.saveCategory(category);
        System.out.println("Обновлено " + category);
        return "redirect:/categories";
    }

    @PostMapping("/add_category")
    public String addDishCatPost(@Valid DishCategory category, BindingResult result) {
        System.out.println("Отправлено " + category);
        if (result.hasErrors())
            return "/Director/add_category";
        service.saveCategory(category);
        System.out.println("Успешно добавлено " + category);
        return "redirect:/categories";
    }

    @GetMapping("/category_remove")
    public String removeCategory(@RequestParam Long id) {
        if (!service.existsWithId(id))
            throw new NoSuchElementException();
        service.removeById(id);
        return "redirect:/categories";
    }
}
