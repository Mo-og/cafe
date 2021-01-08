package ua.opu.kurs_gorbik_kozyrevych.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.opu.kurs_gorbik_kozyrevych.User;
import ua.opu.kurs_gorbik_kozyrevych.services.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.NoSuchElementException;


@Controller
public class UserController {

    private UserService service;

    @Autowired
    public void setService(UserService service) {
        this.service = service;
    }

    @Autowired
    UserService userService;

    @GetMapping("/users")
    public String getWorkers(Model model, Principal principal) {

        try{
            model.addAttribute("workers", service.getAllUsers());

            final UserDetails user = userService.loadUserByUsername(principal.getName());

            switch(user.getAuthorities().toString()) {
                case "[ROLE_WAITER]":  return "Waiter/workers";
                case "[ROLE_COOK]":  return "Cook/workers";
                case "[ROLE_ADMIN]":  return "Director/workers";
            }
        }
        catch (NullPointerException e) {
            return  "User/menu";
        }
        return "User/menu";
    }

    @GetMapping("/worker_edit")
    public String editWorker(Model model, @RequestParam Long id) {
        User user = service.getById(id);
        model.addAttribute("worker", user);
        return "Director/edit_worker";
    }

    //на случай сброса базы - добавляет сотрудника
    @GetMapping("/supersecretrequest7355")
    public String addAdmin(Model model) {
        User user = new User("admin", "admin", "admin", "a@b.c", "991122334455", "address", "директор", "password");
        user.setRoles("ROLE_ADMIN");
        user.setPassword(new BCryptPasswordEncoder().encode("74553211"));
        service.saveUser(user);

        return "redirect:/entrance";
    }


    @GetMapping("/worker_remove")
    public String removeWorker(@RequestParam Long id) {
        if (!service.existsWithId(id))
            throw new NoSuchElementException();
        service.removeById(id);
        return "redirect:/users";
    }

    @GetMapping("/add_worker")
    public String addWorker(Model model) {
        model.addAttribute("worker", new User());
        return "Director/add_worker";
    }

    @PostMapping("/worker_update")
    public String editingSubmit(@Valid User user, BindingResult result) {
        if (result.hasErrors()) {
            return "Director/edit_worker";
        }
        //если пароль не поменяли, то хешировать снова не стоит, если поменяли, то хешируем
        if (!user.getPassword().equals(service.getById(user.getId()).getPassword())) {
            user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        }
        service.saveUser(user);
        return "redirect:/users";
    }

    @PostMapping("/add_worker")
    public String greetingSubmit(@Valid User user, BindingResult result) {
        if (result.hasErrors()) {
            return "Director/add_worker";
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
        service.saveUser(user);
        return "redirect:/users";
    }
}
