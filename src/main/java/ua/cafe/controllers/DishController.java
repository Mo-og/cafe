package ua.cafe.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.cafe.entities.Dish;
import ua.cafe.entities.JsonMaker;
import ua.cafe.entities.Role;
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

    @GetMapping("/api/menu")
    public ResponseEntity<String> getMenuJson() {
        List<Dish> dishes = dishService.getAllDishes();
        for (Dish dish : dishes) {
            dish.setDetails(null); //to avoid cyclic referencing in JSON [dish->details->dish...]
        }
        return JsonMaker.getJsonResponse(dishes);
    }

    @GetMapping("/api/dish")
    public ResponseEntity<String> getDishById(@RequestParam Long id) {
        return JsonMaker.getJsonResponse(dishService.getById(id));
    }

    //update
    @RequestMapping(value = "/api/dish", method = RequestMethod.PUT)
    public ResponseEntity<String> updateDish(@Valid Dish dish, BindingResult result, Principal principal) {
        /*Role role = new Role(principal);
        if (!role.isAdmin())
            return new ResponseEntity<>("You have no permission to change dishes.", HttpStatus.FORBIDDEN);*/
        if (result.hasErrors())
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        dishService.saveDish(dish);
        System.out.println("Dish updated: " + dish);
        return ResponseEntity.ok("Dish updated!");
    }

    //add
    @PostMapping("/api/dish")
    public ResponseEntity<String> apiAddDish(@Valid Dish dish, BindingResult result, Principal principal) {
        /*Role role = new Role(principal);
        if (!role.isAdmin())
            return new ResponseEntity<>("You have no permission to add dishes.", HttpStatus.FORBIDDEN);*/
        if (result.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
        dishService.saveDish(dish);
        return ResponseEntity.ok("Dish saved!");
    }

    @RequestMapping(value = "/api/dish", method = RequestMethod.DELETE)
    public ResponseEntity<String> apiRemoveDish(@RequestParam Long id, Principal principal) {
        /*Role role = new Role(principal);
        if (!role.isAdmin())
            return new ResponseEntity<>("You have no permission to remove dishes.", HttpStatus.FORBIDDEN);*/
        if (!dishService.existsWithId(id))
            return new ResponseEntity<>("No dish was found by given id",HttpStatus.NOT_FOUND);
        dishService.removeById(id);
        return ResponseEntity.ok("Dish removed!");
    }

    /////////////////////////////////////////////////////
    @GetMapping("/menu")
    public String getMenu(Model model, Principal principal) {
        try {
            model.addAttribute("dishes", dishService.getAllDishes());
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

    ///////////////////////////////////////////////////////////////////////////
    @GetMapping("/add_dish")
    public String addDish(Model model, Principal principal) {
        Role role = new Role(principal);
        if (!role.isAdmin())
            return "/permissionDenied";
        model.addAttribute("dish", new Dish());
        model.addAttribute("categories", categoriesService.getAllCategories());
        return "Director/add_dish";
    }

    @GetMapping("/dish_edit")
    public String editDish(Model model, @RequestParam Long id, Principal principal) {
        Role role = new Role(principal);
        if (!role.isAdmin())
            return "/permissionDenied";
        Dish dish = dishService.getById(id);
        model.addAttribute("dish", dish);
        model.addAttribute("categories", categoriesService.getAllCategories());
        System.out.println("Получено " + dish);
        return "Director/edit_dish";
    }

    @PostMapping("/dish_update")
    public String updateDish1(@Valid Dish dish, BindingResult result, Model model, Principal principal) {
        Role role = new Role(principal);
        if (!role.isAdmin())
            return "/permissionDenied";
        if (result.hasErrors()) {
            model.addAttribute("categories", categoriesService.getAllCategories());
            return "Director/edit_dish";
        }
        dishService.saveDish(dish);
        System.out.println("Обновлено " + dish);
        return "redirect:/menu";
    }

    @PostMapping("/add_dish")
    public String greetingSubmit(@Valid Dish dish, BindingResult result, Model model, Principal principal) {
        Role role = new Role(principal);
        if (!role.isAdmin())
            return "/permissionDenied";
        if (result.hasErrors()) {
            model.addAttribute("categories", categoriesService.getAllCategories());
            return "Director/add_dish";
        }
        dishService.saveDish(dish);
        return "redirect:/menu";
    }

    @GetMapping("/dish_remove")
    public String removeDish(@RequestParam Long id, Principal principal) {
        Role role = new Role(principal);
        if (!role.isAdmin())
            return "/permissionDenied";
        if (!dishService.existsWithId(id))
            throw new NoSuchElementException();
        dishService.removeById(id);
        return "redirect:/menu";
    }
}
