package ua.cafe.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.cafe.models.Authority;
import ua.cafe.models.User;
import ua.cafe.services.UserService;
import ua.cafe.utils.JsonMaker;

import javax.validation.Valid;
import java.util.NoSuchElementException;

import static ua.cafe.utils.Stats.markPage;


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

    @RequestMapping(value = {"/users"}, method = RequestMethod.GET)
    public String getUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        markPage(model, "users");
        return "users";
    }

    @GetMapping("/user_edit")
    public String editUser(Model model, @RequestParam Long id, Authentication authentication) {
        User user = userService.getById(id);
        try {
            User auth = (User) authentication.getPrincipal();
            if (auth.getPosition() != Authority.DIRECTOR)
                if (!user.getUsername().equals(auth.getUsername()))
                    return "redirect:/users";
        } catch (NullPointerException e) {
            //noinspection SpringMVCViewInspection
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "edit_user";
    }

    //на случай сброса базы - добавляет сотрудника
    @GetMapping("/supersecretrequest7355")
    public String addAdmin() {
        userService.createAdmin();
        //noinspection SpringMVCViewInspection
        return "redirect:/login";
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
        return "add_user";
    }

    @PostMapping("/user_update")
    public String editingSubmit(@Valid User user, BindingResult result, Authentication authentication) {
        return userService.checkSaveUser(user, result, authentication);
    }

    @PostMapping("/add_user")
    public String greetingSubmit(@Valid User user, BindingResult result, Authentication authentication) {
        if (result.hasErrors()) {
            return "add_user";
        }
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userService.checkSaveUser(user, result, authentication);
        return "redirect:/users";
    }

    @GetMapping("/api/iscook")
    @ResponseBody
    public boolean isCook(Authentication authentication) {
        if (authentication == null) {
            return false;
        }
        User user = (User) authentication.getPrincipal();
        return user.getAuthorities().contains(Authority.COOK) || user.getAuthorities().contains(Authority.DIRECTOR);
    }
}
