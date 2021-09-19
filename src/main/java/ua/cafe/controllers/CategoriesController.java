package ua.cafe.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.cafe.entities.DishCategory;
import ua.cafe.entities.JsonMaker;
import ua.cafe.entities.Role;
import ua.cafe.services.CategoriesService;
import ua.cafe.services.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.NoSuchElementException;

@Controller
public class CategoriesController {

    private static CategoriesService categoriesService;

    @Autowired
    public void setService(CategoriesService service) {
        categoriesService = service;
    }

    ///////////////////////////////////
    //             API               //
    ///////////////////////////////////
    @GetMapping("/api/categories")
    public ResponseEntity<String> getCategories() {
        return JsonMaker.getJsonResponse(categoriesService.getAllCategories());
    }

    @GetMapping("/api/category")
    public ResponseEntity<String> getCategoryJson(@RequestParam Long id) {
        DishCategory category = categoriesService.getById(id);
        if (category != null)
            return JsonMaker.getJsonResponse(category);
        return new ResponseEntity<>("No category found by given id", HttpStatus.NOT_FOUND);
    }

    //update
    @RequestMapping(value = "/api/category", method = RequestMethod.PUT)
    public ResponseEntity<String> apiUpdateCategory(@Valid DishCategory category, BindingResult result, HttpServletRequest request) {
        Role role = new Role(request);
        if (!role.isAdmin())
            return new ResponseEntity<>("You have no permission to change categories", HttpStatus.FORBIDDEN);
        if (result.hasErrors())
            return new ResponseEntity<>(JsonMaker.getJson(category), HttpStatus.NOT_ACCEPTABLE);
        categoriesService.saveCategory(category);
        System.out.println("Обновлено " + category);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //add
    @PostMapping("/api/category")
    public ResponseEntity<String> apiAddDishCategory(@Valid DishCategory category, BindingResult result, HttpServletRequest request) {
        Role role = new Role(request);
        if (!role.isAdmin())
            return new ResponseEntity<>("You have no permission to add categories", HttpStatus.FORBIDDEN);
        if (result.hasErrors())
            return new ResponseEntity<>(JsonMaker.getJson(result.getAllErrors()), HttpStatus.NOT_ACCEPTABLE);
        categoriesService.saveCategory(category);
        System.out.println("Успешно добавлено " + category);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //remove
    @RequestMapping(value = "/api/category", method = RequestMethod.DELETE)
    public ResponseEntity<String> apiRemoveCategory(@RequestParam Long id, HttpServletRequest request) {
        Role role = new Role(request);
        if (!role.isAdmin())
            return new ResponseEntity<>("You have no permission to remove categories", HttpStatus.FORBIDDEN);
        if (!categoriesService.existsWithId(id))
            return new ResponseEntity<>("No category found by given id", HttpStatus.NOT_FOUND);
        categoriesService.removeById(id);
        System.out.println("Removed category with id = " + id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    ////////////////////////////////////////////////////////////////////////////

    @GetMapping("/categories")
    public String getCategories(Model model, HttpServletRequest request) {
        model.addAttribute("categories", categoriesService.getAllCategories());
        if (request.isUserInRole("ROLE_ADMIN")) return "Director/categories";
        if (request.isUserInRole("ROLE_WAITER")) return "Waiter/categories";
        return "index";
    }

    @GetMapping("/add_category")
    public String addDishCategory(Model model) {
        model.addAttribute("category", new DishCategory());
        return "Director/add_category";
    }

    @GetMapping("/category_edit")
    public String editDishCategory(Model model, @RequestParam Long id) {
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
