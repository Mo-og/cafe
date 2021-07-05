package ua.cafe.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ua.cafe.entities.EntranceForm;
import ua.cafe.services.UserService;

import javax.validation.Valid;
@CrossOrigin
@RestController
public class EntranceController {

    @Autowired
    public UserService service;

    @GetMapping("/logout")
    public String logOut() {
        return "User/index";
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
            if (!service.existsWithUsername(entrance.getUsername())) {
                model.addAttribute("messageUsername", "Пользователь не найден");
                model.addAttribute("usernameFailed", true);
            }
            return "User/entrance";
        }
        if (service.existsWithUsername(entrance.getUsername())) {
            model.addAttribute("messagePassword", "Введён неверный пароль!");
            model.addAttribute("passwordFailed", true);
        } else {
            model.addAttribute("messageUsername", "Пользователь не найден");
            model.addAttribute("usernameFailed", true);
        }
        return "User/entrance";

    }
}
