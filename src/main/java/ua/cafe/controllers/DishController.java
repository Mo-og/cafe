package ua.cafe.controllers;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ua.cafe.CafeApplication;
import ua.cafe.entities.Dish;
import ua.cafe.entities.JsonMaker;
import ua.cafe.entities.Role;
import ua.cafe.services.CategoriesService;
import ua.cafe.services.DishService;
import ua.cafe.services.ImageFileService;
import ua.cafe.services.UserService;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;
import java.util.NoSuchElementException;

@Controller
public class DishController {
    private static final String IMAGES_FOLDER_PATH = ".//src//main//resources//static//DishImages//";

    //Services assignment
    private static DishService dishService;
    private static ImageFileService imageService;
    private static UserService userService;
    private static CategoriesService categoriesService;

    @Autowired
    public void setService(DishService service) {
        dishService = service;
    }

    @Autowired
    public void setService(ImageFileService service) {
        imageService = service;
    }

    @Autowired
    public void setService(UserService service) {
        userService = service;
    }

    @Autowired
    public void setService(CategoriesService service) {
        categoriesService = service;
    }

    //API
    @GetMapping("/api/menu")
    public ResponseEntity<String> getMenuJson() {
        List<Dish> dishes = dishService.getAllDishes();
        for (Dish dish : dishes) {
            dish.setDetails(null); //to avoid cyclic referencing in JSON [dish->details->dish...]
        }
        return JsonMaker.getJsonResponse(dishes);
    }

    @GetMapping(value = "/api/image", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody
    ResponseEntity<byte[]> getImage(@RequestParam String path) {
        byte[] bytes;
        try {
            bytes = Files.readAllBytes(Paths.get(IMAGES_FOLDER_PATH.substring(0, 33), path));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(bytes);
    }

    @GetMapping("/api/dish")
    public ResponseEntity<String> getDishById(@RequestParam Long id) {
        return JsonMaker.getJsonResponse(dishService.getById(id));
    }

    //update
    @RequestMapping(value = "/api/dish", method = RequestMethod.PUT)
    public ResponseEntity<String> updateDish(@Valid Dish dish, BindingResult result) {
        if (result.hasErrors())
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        dish.setName(dish.getName().replace("\"","'"));
        dishService.saveDish(dish);
        System.out.println("Dish updated: " + dish);
        return ResponseEntity.ok("Dish updated!");
    }

    //add
    @PostMapping("/api/dish")
    public ResponseEntity<String> apiAddDish(@Valid Dish dish, BindingResult result) {
        if (result.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
        dish.setName(dish.getName().replace("\"","'"));
        dishService.saveDish(dish);
        return ResponseEntity.ok("Dish saved!");
    }

    @RequestMapping(value = "/api/dish", method = RequestMethod.DELETE)
    public ResponseEntity<String> apiRemoveDish(@RequestParam Long id, Principal principal) {
        /*Role role = new Role(principal);
        if (!role.isAdmin())
            return new ResponseEntity<>("You have no permission to remove dishes.", HttpStatus.FORBIDDEN);*/
        if (!dishService.existsWithId(id))
            return new ResponseEntity<>("No dish was found by given id", HttpStatus.NOT_FOUND);
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
    public String editDish(Model model, @RequestParam Long id) {
        Dish dish = dishService.getById(id);
        model.addAttribute("dish", dish);
        if (dish.getImagePath() != null)
            try {
                model.addAttribute("image", Base64.encodeBase64String(Files.readAllBytes(Paths.get(IMAGES_FOLDER_PATH + dish.getImagePath().substring(11)))));
            } catch (IOException e) {
                System.out.println("Incorrect image path for dish " + dish.getName());
            }
        model.addAttribute("categories", categoriesService.getAllCategories());
        System.out.println("Edited " + dish);
        return "Director/edit_dish";
    }

    @PostMapping("/dish_update")
    public String updateDish1(@RequestParam("file") MultipartFile file, @Valid Dish dish, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoriesService.getAllCategories());
            return "Director/edit_dish";
        }
        dish.setName(dish.getName().replace("\"","'"));
        Path path = Paths.get(IMAGES_FOLDER_PATH + dish.getName() + CafeApplication.getDateString());
        try {
            /*String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
            image = new FileDB(fileName, file.getContentType(), file.getBytes());*/

            byte[] bytes = file.getBytes();
            Files.write(path, bytes);
        } catch (IOException e) {
            System.out.println("Dish image update wasn't successful due to invalid image file");
            model.addAttribute("categories", categoriesService.getAllCategories());
            return "Director/edit_dish";
        }
        try {
            if (dish.getImagePath() != null)
                Files.delete(Paths.get(IMAGES_FOLDER_PATH, dish.getImagePath().substring(11)));
        } catch (IOException e) {
            System.out.println("Unable to delete old DishImage");
            e.printStackTrace();
        }
        dish.setImagePath(path.toString().substring(28).replace("\\", "/"));
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
        dish.setName(dish.getName().replace("\"","'"));
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
