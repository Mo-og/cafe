package ua.cafe.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ua.cafe.models.Dish;
import ua.cafe.services.CategoriesService;
import ua.cafe.services.DishService;
import ua.cafe.utils.JsonMaker;
import ua.cafe.utils.ResponseFactory;
import ua.cafe.utils.Stats;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.NoSuchElementException;

import static ua.cafe.utils.Stats.markPage;

@Slf4j
@Controller
public class DishController {
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

    //
    //API
    //

    //get all dishes
    @GetMapping("/api/dishes")
    public ResponseEntity<String> getMenuJson() {
        log.info("/api/dishes");
        List<Dish> dishes = dishService.getAllDishes();
        return JsonMaker.getJsonResponse(dishes);
    }

    //get all dishes, image link for thumbnail
    @GetMapping("/api/dishes_th")
    public ResponseEntity<String> getMenuWithThumbnailJson() {
        List<Dish> dishes = dishService.getAllDishes();
        dishes.forEach(dish -> dish.setThumb(true));
        return JsonMaker.getJsonResponse(dishes);
    }

    //get image
    @GetMapping(value = "/DishImages/{image}", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody
    ResponseEntity<byte[]> getImage(@PathVariable String image) {
        byte[] bytes;
        try {
            bytes = Files.readAllBytes(Paths.get(Stats.IMAGES_FOLDER_PATH, image));
            return ResponseEntity.ok(bytes);
        } catch (IOException ex) {
            System.out.println("* No image found with name '" + image + "'");
            return ResponseEntity.notFound().build();
        }
    }

    //get thumbnail
    @GetMapping(value = Stats.URL_THUMBNAILS_PATH + "{image}", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody
    ResponseEntity<byte[]> getThumbnail(@PathVariable String image) {
        byte[] bytes;
        try {
            bytes = Files.readAllBytes(Paths.get(Stats.IMAGES_THUMBNAILS_PATH, image));
            return ResponseEntity.ok(bytes);
        } catch (IOException e) {
            try {
                bytes = Files.readAllBytes(Paths.get(Stats.IMAGES_THUMBNAILS_PATH, image + ".jpg"));
                return ResponseEntity.ok(bytes);
            } catch (IOException ex) {
                System.out.println("* No image found with name '" + image + "'");
                return ResponseEntity.notFound().build();
            }
        }
    }

    //get dish by id
    @GetMapping("/api/dish")
    public ResponseEntity<String> getDishById(@RequestParam Long id) {
        return JsonMaker.getJsonResponse(dishService.getById(id));
    }

    //POST, PUT dish
    @RequestMapping(value = "/api/dish", method = {RequestMethod.PUT, RequestMethod.POST})
    public ResponseEntity<?> apiUpdateDish(@Valid Dish dish, BindingResult result) {
        ResponseEntity<?> ErrorsMap = ResponseFactory.createResponse(result);
        if (ErrorsMap != null) return ErrorsMap;
        dish.setName(dish.getName().replace("\"", "'"));
        dishService.saveDish(dish);
        return ResponseEntity.ok("Dish updated!");
    }

    //delete dish by id
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
        markPage(model, "menuStaff");
        return "menuStaff";
    }

    @GetMapping("/add_dish")
    public String addDish(Model model) {
        model.addAttribute("dish", new Dish());
        model.addAttribute("categories", categoriesService.getAllCategories());
        return "add_dish";
    }

    @GetMapping("/dish_edit")
    public String editDish(Model model, @RequestParam Long id) {
        Dish dish = dishService.getById(id);
        model.addAttribute("dish", dish);
        model.addAttribute("categories", categoriesService.getAllCategories());
        return "edit_dish";
    }

    @PostMapping("/dish_update")
    public String updateDish(@RequestParam("file") MultipartFile file, @Valid Dish dish, BindingResult result, long categoryId, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoriesService.getAllCategories());
            return "edit_dish";
        }
        categoriesService.setById(dish, categoryId);
        dishService.resolveImage(file, dish);
        dishService.saveDish(dish);
        log.info("Обновлено " + dish);
        return "redirect:/menu_staff";
    }

    @PostMapping("/add_dish")
    public String greetingSubmit(@RequestParam("file") MultipartFile file, @Valid Dish dish, BindingResult result, long categoryId, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoriesService.getAllCategories());
            return "add_dish";
        }
        categoriesService.setById(dish, categoryId);
        dishService.resolveImage(file, dish);
        dishService.saveDish(dish);
        return "redirect:/menu_staff";
    }

    @GetMapping("/dish_remove")
    public String removeDish(@RequestParam Long id) {
        if (dishService.isNoneWithId(id))
            throw new NoSuchElementException();
        dishService.removeById(id);
        return "redirect:/menu_staff";
    }
}
