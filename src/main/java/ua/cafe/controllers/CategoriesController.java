package ua.cafe.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.cafe.entities.Dish;
import ua.cafe.services.CategoriesService;
import ua.cafe.services.UserService;
import ua.cafe.entities.DishCategory;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.NoSuchElementException;

@Controller
public class CategoriesController {

    private static CategoriesService categoriesService;
    private static UserService userService;

    @Autowired
    public void setService(UserService service) {
        userService = service;
    }

    @Autowired
    public void setService(CategoriesService service) {
        categoriesService = service;
    }

    @RequestMapping(value = "/categoriesJSON", method = RequestMethod.GET,
            produces = "application/json")
    @ResponseBody
    public String getMenuJson() {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = null;
        try {
            json = ow.writeValueAsString(categoriesService.getAllCategories());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }

    @GetMapping("/categories")
    public String getCategory(Model model, Principal principal) {

        try {
            final UserDetails user = userService.loadUserByUsername(principal.getName());
            model.addAttribute("categories", categoriesService.getAllCategories());
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
        DishCategory category = categoriesService.getById(id);
        model.addAttribute("category", category);
        System.out.println("Получено " + category);
        return "Director/edit_category";
    }

    @PostMapping("/category_update")
    public String updateDish(@Valid DishCategory category, BindingResult result) {
        if (result.hasErrors())
            return "Director/edit_category";
        categoriesService.saveCategory(category);
        System.out.println("Обновлено " + category);
        return "redirect:/categories";
    }

    @PostMapping("/add_category")
    public String addDishCatPost(@Valid DishCategory category, BindingResult result) {
        System.out.println("Отправлено " + category);
        if (result.hasErrors())
            return "/Director/add_category";
        categoriesService.saveCategory(category);
        System.out.println("Успешно добавлено " + category);
        return "redirect:/categories";
    }

    @GetMapping("/category_remove")
    public String removeCategory(@RequestParam Long id) {
        if (!categoriesService.existsWithId(id))
            throw new NoSuchElementException();
        categoriesService.removeById(id);
        return "redirect:/categories";
    }
}
