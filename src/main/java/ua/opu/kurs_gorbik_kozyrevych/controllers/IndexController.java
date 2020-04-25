package ua.opu.kurs_gorbik_kozyrevych.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/")
    public String index() {
        return "User/index";
    }

    @GetMapping("/User/index")
    public String user_index() {
        return "User/index";
    }


    @GetMapping("/Director/index")
    public String admin_index() {
        return "Director/index";
    }

    @GetMapping("/Waiter/index")
    public String waiter_index() {
        return "Waiter/index";
    }

    @GetMapping("/Cook/index")
    public String cook_index() {
        return "Cook/index";
    }

}