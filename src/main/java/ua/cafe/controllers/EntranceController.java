package ua.cafe.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.cafe.entities.EntranceForm;
import ua.cafe.entities.Role;
import ua.cafe.entities.User;
import ua.cafe.services.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@CrossOrigin
@Controller
public class EntranceController {

    private static UserService userService;

    @Autowired
    public void setService(UserService service) {
        userService = service;
    }

    @RequestMapping(value = "/username", method = RequestMethod.GET)
    public String currentUserName(Authentication authentication, Model model) {
        if (authentication == null) return "test";
        User user = (User) authentication.getPrincipal();
        System.out.println(user);
        model.addAttribute("name", user.getUsername());
        System.out.println(authentication.getAuthorities());
        System.out.println(user.getAuthorities().iterator().next());
        return "test";
    }

    @GetMapping("/")
    public String entrance(HttpServletRequest request, Model model, Authentication authentication) {
        model.addAttribute("role", new Role(request));
        return "index";
    }

    @GetMapping(value = "/img/{image}", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody
    ResponseEntity<byte[]> getImage(@PathVariable String image) {
        byte[] bytes;
        try {
            bytes = Files.readAllBytes(Paths.get(".//src//main//resources//static//img//", image));
            return ResponseEntity.ok(bytes);
        } catch (IOException e) {
            System.out.println("* No image found with name '" + image + "'");
            try {
                bytes = Files.readAllBytes(Paths.get(".//src//main//resources//static//images//no-dish-image.png"));
                return ResponseEntity.ok(bytes);
            } catch (IOException ex) {
                System.out.println("* Broken link for no-dish-image icon");
                ex.printStackTrace();
            }
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/logout")
    public String logOut(Model model) {
        model.addAttribute("role", new Role());
        return "logout";
    }

    @GetMapping("/entrance")
    public String getEntrance(Model model) {
        model.addAttribute("entrance", new EntranceForm());
        return "User/entrance";
    }

    @PostMapping("/entrance")
    public String validateEntrance(@Valid EntranceForm entrance, BindingResult result, Model model) {
        model.addAttribute("entrance", entrance);
        if (result.hasFieldErrors("username")) {
            model.addAttribute("messageUsername", "Некорректный номер телефона!");
            model.addAttribute("usernameFailed", true);
            if (result.hasFieldErrors("password")) {
                model.addAttribute("messagePassword", "Недопустимый пароль!");
                model.addAttribute("passwordFailed", true);
            }
            return "User/entrance";
        }
        if (result.hasFieldErrors("password")) {
            model.addAttribute("messagePassword", "Недопустимый пароль!");
            model.addAttribute("passwordFailed", true);
            if (!userService.existsWithUsername(entrance.getUsername())) {
                model.addAttribute("messageUsername", "Пользователь не найден");
                model.addAttribute("usernameFailed", true);
            }
            return "User/entrance";
        }
        if (userService.existsWithUsername(entrance.getUsername())) {
            model.addAttribute("messagePassword", "Введён неверный пароль!");
            model.addAttribute("passwordFailed", true);
        } else {
            model.addAttribute("messageUsername", "Пользователь не найден");
            model.addAttribute("usernameFailed", true);
        }
        return "User/entrance";

    }
}
