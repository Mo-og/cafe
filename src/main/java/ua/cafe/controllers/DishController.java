package ua.cafe.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ua.cafe.CafeApplication;
import ua.cafe.Translit;
import ua.cafe.entities.Dish;
import ua.cafe.entities.ImageProcessor;
import ua.cafe.entities.JsonMaker;
import ua.cafe.entities.User;
import ua.cafe.services.CategoriesService;
import ua.cafe.services.DishService;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Controller
public class DishController {
    private static final String IMAGES_FOLDER_PATH = ".//src//main//resources//static//DishImages//";
    public static final String IMAGES_THUMBNAILS_PATH = ".//src//main//resources//static//DishImages//thumbnails//";
    public static final String URL_THUMBNAILS_PATH = "/DishImages/thumbnails/";
    //Services assignment
    private static DishService dishService;
    private static CategoriesService categoriesService;

    @Autowired
    public void setService(DishService service) {
        dishService = service;
    }

    @Autowired
    public void setService(CategoriesService service) {
        categoriesService = service;
    }

    //API
    @GetMapping("/api/menu")
    public ResponseEntity<String> getMenuJson() {
        List<Dish> dishes = dishService.getAllDishes();
        return JsonMaker.getJsonResponse(dishes);
    }

    @GetMapping("/api/menu_th")
    public ResponseEntity<String> getMenuWithThumbnailJson() {
        List<Dish> dishes = dishService.getAllDishes();
        for (Dish dish : dishes)
            dish.setThumb(true);
        return JsonMaker.getJsonResponse(dishes);
    }

    @GetMapping(value = "/DishImages/{image}", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody
    ResponseEntity<byte[]> getImage(@PathVariable String image) {
        byte[] bytes;
        try {
            bytes = Files.readAllBytes(Paths.get(IMAGES_FOLDER_PATH, image));
            return ResponseEntity.ok(bytes);
        } catch (IOException e) {
//            e.printStackTrace();
            System.out.println("* No image found with name '" + image + "'");
            /*try {
                bytes = Files.readAllBytes(Paths.get(".//src//main//resources//static//images//dish-loading.gif"));
                return ResponseEntity.ok(bytes);
            } catch (IOException ex) {
                System.out.println("* Broken link for spinner");
                ex.printStackTrace();
            }*/
            return ResponseEntity.notFound().build();
        }
        /*System.out.println("* Something went wrong in getImage controller");
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);*/
    }

    @GetMapping(value = URL_THUMBNAILS_PATH + "{image}", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody
    ResponseEntity<byte[]> getThumbnail(@PathVariable String image) {
        byte[] bytes;
        try {
            bytes = Files.readAllBytes(Paths.get(IMAGES_THUMBNAILS_PATH, image));
            return ResponseEntity.ok(bytes);
        } catch (IOException e) {
//            e.printStackTrace();
            System.out.println("* No image found with name '" + image + "'");
            /*try {
                bytes = Files.readAllBytes(Paths.get(".//src//main//resources//static//images//dish-loading.gif"));
                return ResponseEntity.ok(bytes);
            } catch (IOException ex) {
                System.out.println("* Broken link for spinner");
                ex.printStackTrace();
            }*/
            return ResponseEntity.notFound().build();
        }
        /*System.out.println("* Something went wrong in getImage controller");
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);*/
    }

    @GetMapping("/api/dish")
    public ResponseEntity<String> getDishById(@RequestParam Long id) {
        return JsonMaker.getJsonResponse(dishService.getById(id));
    }

    //update
    @RequestMapping(value = "/api/dish", method = RequestMethod.PUT)
    public ResponseEntity<String> apiUpdateDish(@Valid Dish dish, BindingResult result) {
        if (result.hasErrors())
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        dish.setName(dish.getName().replace("\"", "'"));
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
        dish.setName(dish.getName().replace("\"", "'"));
        dishService.saveDish(dish);
        return ResponseEntity.ok("Dish saved!");
    }

    @RequestMapping(value = "/api/dish", method = RequestMethod.DELETE)
    public ResponseEntity<String> apiRemoveDish(@RequestParam Long id, Principal principal) {
        /*Role role = new Role(principal);
        if (!role.isAdmin())
            return new ResponseEntity<>("You have no permission to remove dishes.", HttpStatus.FORBIDDEN);*/
        if (dishService.isNoneWithId(id))
            return new ResponseEntity<>("No dish was found by given id", HttpStatus.NOT_FOUND);
        dishService.removeById(id);
        return ResponseEntity.ok("Dish removed!");
    }

    /////////////////////////////////////////////////////
    @GetMapping("/menu")
    public String getMenu(Model model, Authentication authentication) {
        if (authentication != null) {
            var dishes = dishService.getAllDishes();
            dishes.forEach(d -> d.setThumb(true));
            model.addAttribute("dishes", dishes);
        } else return "User/menu";
        return switch (((User) authentication.getPrincipal()).getAuthorities().get(0)) {
            case WAITER -> "Waiter/menu";
            case DIRECTOR -> "Director/menu";
            case COOK -> "Cook/menu";
        };
    }

    ///////////////////////////////////////////////////////////////////////////
    @GetMapping("/add_dish")
    public String addDish(Model model) {
        model.addAttribute("dish", new Dish());
        model.addAttribute("categories", categoriesService.getAllCategories());
        return "Director/add_dish";
    }

    @GetMapping("/dish_edit")
    public String editDish(Model model, @RequestParam Long id) {
        Dish dish = dishService.getById(id);
        model.addAttribute("dish", dish);
        /*if (dish.getImagePath() != null)
            try {
                model.addAttribute("image", Base64.encodeBase64String(Files.readAllBytes(Paths.get(IMAGES_FOLDER_PATH + dish.getImagePath().substring(11)))));
            } catch (IOException e) {
                System.out.println("Incorrect image path for dish " + dish.getName());
            }*/
        model.addAttribute("categories", categoriesService.getAllCategories());
        System.out.println("Edited " + dish);
        return "Director/edit_dish";
    }

    @PostMapping("/dish_update")
    public String updateDish(@RequestParam("file") MultipartFile file, @Valid Dish dish, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoriesService.getAllCategories());
            return "Director/edit_dish";
        }
        operateDish(file, dish, model);
        System.out.println("Обновлено " + dish);
        return "redirect:/menu";
    }

    @PostMapping("/add_dish")
    public String greetingSubmit(@RequestParam("file") MultipartFile file, @Valid Dish dish, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoriesService.getAllCategories());
            return "Director/add_dish";
        }
        operateDish(file, dish, model);
        return "redirect:/menu";
    }

    private void operateDish(MultipartFile file, Dish dish, Model model) {
        dish.setName(dish.getName().replace("\"", "'"));
        Path path = null;
        try {
            if (file.isEmpty()) throw new NullPointerException();
            int index = Objects.requireNonNull(file.getOriginalFilename()).lastIndexOf('.');
            if (index == -1) index = 0;
            //generating file name
            String pathString = Translit.cyr2lat(dish.getName()) + CafeApplication.getDateString() + Objects.requireNonNull(file.getOriginalFilename()).substring(index);
            path = Paths.get(IMAGES_FOLDER_PATH + pathString);
            byte[] bytes = file.getBytes();
            Files.write(path, bytes);
            ImageProcessor.saveThumbnail(file.getInputStream(), pathString);
            System.out.println("Saved thumnail successfully");
        } catch (IOException | NullPointerException e) {
            System.out.println("*-Dish image update wasn't successful due to invalid image file.\n*-Error: " + e.getMessage());
            model.addAttribute("categories", categoriesService.getAllCategories());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (path != null && dish.getImagePath() != null && dish.getImagePath().length() > 11) {
            try {
                Files.delete(Paths.get(IMAGES_FOLDER_PATH, dish.getImagePath().substring(11)));
                Files.delete(Paths.get(IMAGES_THUMBNAILS_PATH, dish.getImagePath().substring(11)));
                System.out.println("Removed old dish images: " + dish.getImagePath().substring(11));
            } catch (IOException | InvalidPathException e) {
                System.out.println("Unable to delete old DishImage: " + e.getMessage());
                e.printStackTrace();
            }
            dish.setImagePath(path.toString().substring(28).replace("\\", "/"));
        }
        dishService.saveDish(dish);
    }

    @GetMapping("/dish_remove")
    public String removeDish(@RequestParam Long id) {
        if (dishService.isNoneWithId(id))
            throw new NoSuchElementException();
        dishService.removeById(id);
        return "redirect:/menu";
    }
}
