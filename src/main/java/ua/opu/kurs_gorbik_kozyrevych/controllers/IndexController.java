package ua.opu.kurs_gorbik_kozyrevych.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ua.opu.kurs_gorbik_kozyrevych.services.WorkerService;

import java.security.Principal;

@Controller
public class IndexController {

    @Autowired
    WorkerService workerService;



    @GetMapping("/")
    public String entrance(Principal principal) {
        try{
            final UserDetails user = workerService.loadUserByUsername(principal.getName());
            switch(user.getAuthorities().toString()) {

                case "[ROLE_WAITER]":
                     return "Waiter/index";
                case "[ROLE_COOK]":  return "Cook/index";
                case "[ROLE_ADMIN]":  return "Director/index";
                default: System.out.println(user.getAuthorities().toString());
            }
        }
        catch (NullPointerException e) {
            return  "User/index";
        }
        return  "User/index";
    }


}