package ua.cafe.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ua.cafe.entities.EntranceForm;
import ua.cafe.entities.Role;
import ua.cafe.entities.User;
import ua.cafe.services.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;

@CrossOrigin
@Controller
public class EntranceController {

    private static UserService userService;

    @Autowired
    public void setService(UserService service) {
        userService = service;
    }

    @GetMapping("/")
    public String entrance(HttpServletRequest request, Model model) {
        model.addAttribute("role", new Role(request));
        return "index";
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
