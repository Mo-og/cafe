package ua.cafe.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ua.cafe.entities.Role;
import ua.cafe.services.UserService;

import java.security.Principal;

@Controller
public class IndexController {

    private static UserService userService;

    @Autowired
    public void setService(UserService service) {
        userService = service;
    }


    @GetMapping("/")
    public String entrance(Principal principal, Model model) {
        try {
            final UserDetails user = userService.loadUserByUsername(principal.getName());
            model.addAttribute("userStatus", new Role(user.getAuthorities().toString()));
            switch (user.getAuthorities().toString()) {

                case "[ROLE_WAITER]":
                    return "Waiter/index";
                case "[ROLE_COOK]":
                    return "Cook/index";
                case "[ROLE_ADMIN]":
                    return "Director/index";
                default:
                    System.out.println(user.getAuthorities().toString());
            }
        } catch (NullPointerException e) {
            return "User/index";
        }
        return "User/index";
    }


}