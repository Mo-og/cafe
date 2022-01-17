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
import ua.cafe.entities.User;
import ua.cafe.services.UserService;
import ua.cafe.utils.Role;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Principal;

@CrossOrigin
@Controller
public class LoginController {

    private static UserService userService;

    @Autowired
    public void setService(UserService service) {
        userService = service;
    }

    @RequestMapping(value = "/username", method = RequestMethod.GET)
    public String currentUserName(Principal principal, Model model, Authentication authentication) {
        model.addAttribute("name", ((User) authentication.getPrincipal()).getFirstName());
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

    @GetMapping("/login")
    public String getEntrance(Model model) {
        model.addAttribute("form", new EntranceForm());
        return "login";
    }

    @PostMapping("/login")
    public String validateEntrance(@Valid EntranceForm form, BindingResult result, Model model) {
        model.addAttribute("form", form);
        System.out.println(form);
        result.getFieldErrors().forEach(fieldError -> System.out.println(fieldError.getField()+"; "+fieldError.getDefaultMessage()));
        if (result.hasFieldErrors("username")) {
            model.addAttribute("messageUsername", "Некорректный номер телефона!");
            model.addAttribute("usernameFailed", true);
            if (result.hasFieldErrors("password")) {
                model.addAttribute("messagePassword", "Недопустимый пароль!");
                model.addAttribute("passwordFailed", true);
            }
            return "login";
        }
        if (result.hasFieldErrors("password")) {
            model.addAttribute("messagePassword", "Недопустимый пароль!");
            model.addAttribute("passwordFailed", true);
            if (!userService.existsWithUsername(form.getUsername())) {
                model.addAttribute("messageUsername", "Пользователь не найден");
                model.addAttribute("usernameFailed", true);
            }
            return "login";
        }
        if (userService.existsWithUsername(form.getUsername())) {
            model.addAttribute("messagePassword", "Введён неверный пароль!");
            model.addAttribute("passwordFailed", true);
        } else {
            model.addAttribute("messageUsername", "Пользователь не найден");
            model.addAttribute("usernameFailed", true);
        }
        return "login";

    }
}
