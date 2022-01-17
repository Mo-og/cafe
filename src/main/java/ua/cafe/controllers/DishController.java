package ua.cafe.controllers;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ua.cafe.entities.Dish;
import ua.cafe.services.CategoriesService;
import ua.cafe.services.DishService;
import ua.cafe.utils.ImageProcessor;
import ua.cafe.utils.JsonMaker;
import ua.cafe.utils.Translit;
import ua.cafe.utils.Utils;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    @GetMapping("/api/dishes")
    public ResponseEntity<String> getMenuJson() {
        List<Dish> dishes = dishService.getAllDishes();
        return JsonMaker.getJsonResponse(dishes);
    }

    @GetMapping("/api/dishes_th")
    public ResponseEntity<String> getMenuWithThumbnailJson() {
        List<Dish> dishes = dishService.getAllDishes();
        dishes.forEach(dish -> dish.setThumb(true));
        return JsonMaker.getJsonResponse(dishes);
    }

    @GetMapping(value = "/DishImages/{image}", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody
    ResponseEntity<byte[]> getImage(@PathVariable String image) {
        byte[] bytes;
        try {
            bytes = Files.readAllBytes(Paths.get(IMAGES_FOLDER_PATH, image));
            return ResponseEntity.ok(bytes);
        } catch (IOException ex) {
            System.out.println("* No image found with name '" + image + "'");
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = URL_THUMBNAILS_PATH + "{image}", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody
    ResponseEntity<byte[]> getThumbnail(@PathVariable String image) {
        byte[] bytes;
        try {
            bytes = Files.readAllBytes(Paths.get(IMAGES_THUMBNAILS_PATH, image));
            return ResponseEntity.ok(bytes);
        } catch (IOException e) {
            try {
                bytes = Files.readAllBytes(Paths.get(IMAGES_THUMBNAILS_PATH, image + ".jpg"));
                return ResponseEntity.ok(bytes);
            } catch (IOException ex) {
                System.out.println("* No image found with name '" + image + "'");
                return ResponseEntity.notFound().build();
            }
        }
    }

    @GetMapping("/api/dish")
    public ResponseEntity<String> getDishById(@RequestParam Long id) {
        return JsonMaker.getJsonResponse(dishService.getById(id));
    }

    //POST, PUT
    @RequestMapping(value = "/api/dish", method = {RequestMethod.PUT, RequestMethod.POST})
    public ResponseEntity<?> apiUpdateDish(@Valid Dish dish, BindingResult result) {
        ResponseEntity<?> ErrorsMap = Utils.getValidityResponse(result);
        if (ErrorsMap != null) return ErrorsMap;
        dish.setName(dish.getName().replace("\"", "'"));
        dishService.saveDish(dish);
        return ResponseEntity.ok("Dish updated!");
    }

    @RequestMapping(value = "/api/dish", method = RequestMethod.DELETE)
    public ResponseEntity<String> apiRemoveDish(@RequestParam Long id) {
        if (dishService.isNoneWithId(id))
            return new ResponseEntity<>("No dish was found by given id of " + id, HttpStatus.NOT_FOUND);
        dishService.removeById(id);
        return ResponseEntity.ok("Dish removed!");
    }

    /////////////////////////////////////////////////////
    @GetMapping("/menu")
    public String getMenu(Model model) {
        var dishes = dishService.getAllDishes();
        model.addAttribute("dishes", dishes);
        return "menu";
    }

    @GetMapping("/menu_staff")
    public String getStaffMenu(Model model) {
        var dishes = dishService.getAllDishes();
        dishes.forEach(d -> d.setThumb(true));
        model.addAttribute("dishes", dishes);
        return "menuStaff";
    }

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
        model.addAttribute("categories", categoriesService.getAllCategories());
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
        return "redirect:/menu_staff";
    }

    @PostMapping("/add_dish")
    public String greetingSubmit(@RequestParam("file") MultipartFile file, @Valid Dish dish, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoriesService.getAllCategories());
            return "Director/add_dish";
        }
        operateDish(file, dish, model);
        return "redirect:/menu_staff";
    }

    private void operateDish(MultipartFile file, @NotNull Dish dish, Model model) {
        dish.setName(dish.getName().replace("\"", "'"));
        Path path = null;
        try {
            if (file.isEmpty()) throw new NullPointerException();
            int index = Objects.requireNonNull(file.getOriginalFilename()).lastIndexOf('.');
            if (index == -1) index = 0;
            //generating file name
            String pathString = Translit.cyr2lat(dish.getName()) + Utils.getDateString() + Objects.requireNonNull(file.getOriginalFilename()).substring(index);
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
            }
        }
        if (path != null)
            dish.setImagePath(path.toString().substring(28).replace("\\", "/"));
        dishService.saveDish(dish);
    }

    @GetMapping("/dish_remove")
    public String removeDish(@RequestParam Long id) {
        if (dishService.isNoneWithId(id))
            throw new NoSuchElementException();
        dishService.removeById(id);
        return "redirect:/menu_staff";
    }
}
