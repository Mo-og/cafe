package ua.cafe.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.cafe.entities.Authority;
import ua.cafe.entities.User;
import ua.cafe.services.UserService;
import ua.cafe.utils.JsonMaker;

import javax.validation.Valid;
import java.util.NoSuchElementException;

import static ua.cafe.utils.Utils.markPage;


@Controller
public class UserController {

    private UserService userService;

    @Autowired
    public void setUserService(UserService service) {
        userService = service;
    }

    @RequestMapping(value = {"/api/users"}, method = RequestMethod.GET)
    public ResponseEntity<String> apiGetUsers() {
        return JsonMaker.getJsonResponse(userService.getAllUsers());
    }

    @RequestMapping(value = {"/users", "/workers"}, method = RequestMethod.GET)
    public String getUsers(Model model, Authentication authentication) {
        model.addAttribute("users", userService.getAllUsers());
        markPage(model, "users");
        return switch (((User) authentication.getPrincipal()).getPosition()) {
            case WAITER -> "Waiter/users";
            case COOK -> "Cook/users";
            case DIRECTOR -> "Director/users";
        };

    }

    @GetMapping("/user_edit")
    public String editUser(Model model, @RequestParam Long id) {
        User user = userService.getById(id);
        model.addAttribute("user", user);
        return "Director/edit_user";
    }

    //на случай сброса базы - добавляет сотрудника
    @GetMapping("/supersecretrequest7355")
    public String addAdmin() {
        User user = new User("admin", "admin", "admin", "a@b.c", "991122334455", "address", Authority.DIRECTOR, "password");
        user.setPassword(new BCryptPasswordEncoder().encode("74553211"));
        userService.removeByUsername("991122334455");
        userService.saveUser(user);
        return "redirect:/entrance";
    }


    @GetMapping("/user_remove")
    public String removeUser(@RequestParam Long id) {
        if (!userService.existsWithId(id))
            throw new NoSuchElementException();
        userService.removeById(id);
        return "redirect:/users";
    }

    @GetMapping("/add_user")
    public String addUser(Model model) {
        model.addAttribute("user", new User());
        return "Director/add_user";
    }

    @PostMapping("/user_update")
    public String editingSubmit(@Valid User user, BindingResult result) {
        if (result.hasErrors() && !user.getPassword().equals("")) {
            return "Director/edit_user";
        }
        //если пароль не поменяли, то хешировать снова не стоит, если поменяли, то хешируем
        if (user.getPassword().equals("")) {
            user.setPassword(userService.getById(user.getId()).getPassword());
        } else user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userService.saveUser(user);
        return "redirect:/users";
    }

    @PostMapping("/add_user")
    public String greetingSubmit(@Valid User user, BindingResult result) {
        if (result.hasErrors()) {
            return "Director/add_user";
        }
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userService.saveUser(user);
        return "redirect:/users";
    }
}
