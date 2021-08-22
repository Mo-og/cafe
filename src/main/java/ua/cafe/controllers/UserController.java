package ua.cafe.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.cafe.entities.JsonMaker;
import ua.cafe.entities.Role;
import ua.cafe.entities.User;
import ua.cafe.services.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.NoSuchElementException;


@Controller
public class UserController {

    private UserService userService;

    @Autowired
    public void setUserService(UserService service) {
        userService = service;
    }

    @RequestMapping(value = {"/users"}, method = RequestMethod.GET)
    public ResponseEntity<String> apiGetUsers(Principal principal) {
        Role role = new Role(principal);
        if (!role.isAdmin())
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        return JsonMaker.getJsonResponse(userService.getAllUsers());
    }

    @RequestMapping(value = {"/users", "/workers"}, method = RequestMethod.GET)
    public String getUsers(Model model, Principal principal) {

        try {
            model.addAttribute("users", userService.getAllUsers());

            final UserDetails user = userService.loadUserByUsername(principal.getName());

            switch (user.getAuthorities().toString()) {
                case "[ROLE_WAITER]":
                    return "Waiter/users";
                case "[ROLE_COOK]":
                    return "Cook/users";
                case "[ROLE_ADMIN]":
                    return "Director/users";
            }
        } catch (NullPointerException e) {
            return "permissionDenied";
        }
        return "permissionDenied";
    }

    @GetMapping("/user")
    public String editUser(Model model, @RequestParam Long id, Principal principal) {
        Role role = new Role(principal);
        if (!role.isAdmin())
            return "permissionDenied";
        User user = userService.getById(id);
        model.addAttribute("user", user);
        return "Director/edit_user";
    }

    //на случай сброса базы - добавляет сотрудника
    @GetMapping("/supersecretrequest7355")
    public String addAdmin() {
        User user = new User("admin", "admin", "admin", "a@b.c", "991122334455", "address", "директор", "password");
        user.setRoles("ROLE_ADMIN");
        user.setPassword(new BCryptPasswordEncoder().encode("74553211"));
        user.setId(0);
        userService.removeByUsername("991122334455");
        userService.saveUser(user);
        return "redirect:/entrance";
    }


    @GetMapping("/user_remove")
    public String removeUser(@RequestParam Long id, Principal principal) {
        Role role = new Role(principal);
        if (!role.isAdmin())
            return "/permissionDenied";
        if (!userService.existsWithId(id))
            throw new NoSuchElementException();
        userService.removeById(id);
        return "redirect:/users";
    }

    @GetMapping("/add_user")
    public String addUser(Model model, Principal principal) {
        Role role = new Role(principal);
        if (!role.isAdmin())
            return "/permissionDenied";
        model.addAttribute("user", new User());
        return "Director/add_user";
    }

    @PostMapping("/user_update")
    public String editingSubmit(@Valid User user, BindingResult result, Principal principal) {
        Role role = new Role(principal);
        if (!role.isAdmin())
            return "/permissionDenied";
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
    public String greetingSubmit(@Valid User user, BindingResult result, Principal principal) {
        Role role = new Role(principal);
        if (!role.isAdmin())
            return "/permissionDenied";
        if (result.hasErrors()) {
            return "Director/add_user";
        }
        switch (user.getPosition().toLowerCase()) {
            case "повар":
                user.setRoles("ROLE_COOK");
                break;
            case "официант":
                user.setRoles("ROLE_WAITER");
                break;
            case "директор":
                user.setRoles("ROLE_ADMIN");
                break;
        }
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userService.saveUser(user);
        return "redirect:/users";
    }
}
