package ua.cafe.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.cafe.entities.DishCategory;
import ua.cafe.services.CategoriesService;
import ua.cafe.utils.JsonMaker;
import ua.cafe.utils.Utils;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.NoSuchElementException;

@Slf4j
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

    //POST, PUT
    @RequestMapping(value = "/api/category", method = {RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<?> apiAddUpdateCategory(@Valid DishCategory category, BindingResult result) {
        ResponseEntity<?> ErrorsMap = Utils.getValidityResponse(result);
        if (ErrorsMap != null) return ErrorsMap;
        return new ResponseEntity<>(categoriesService.saveCategory(category), HttpStatus.OK);
    }

    //DELETE
    @RequestMapping(value = "/api/category", method = RequestMethod.DELETE)
    public ResponseEntity<String> apiRemoveCategory(@RequestParam Long id) {
        try {
            categoriesService.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            return new ResponseEntity<>("No category found by given id of " + id, HttpStatus.NOT_FOUND);
        }
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
        log.info("Got category " + category);
        if (result.hasErrors()) {
            result.getFieldErrors().forEach(fieldError -> log.error(fieldError.getObjectName() + " [" + fieldError.getRejectedValue() + "]: " + fieldError.getDefaultMessage()));
            return "/Director/add_category";
        }
        log.info("No errors found for " + category.getName());
        categoriesService.saveCategory(category);
        log.info("Успешно добавлено " + category);
        return "redirect:/categories";
    }

    @GetMapping("/category_remove")
    public String removeCategory(@RequestParam Long id) {
        if (!categoriesService.existsWithId(id))
            throw new NoSuchElementException();
        categoriesService.deleteById(id);
        return "redirect:/categories";
    }
}
