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
import ua.opu.kurs_gorbik_kozyrevych.Worker;
import ua.opu.kurs_gorbik_kozyrevych.services.WorkerService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;


@Controller
public class WorkerController {

    private WorkerService service;

    @Autowired
    public void setService(WorkerService service) {
        this.service = service;
    }
    @Autowired
    WorkerService workerService;

    @GetMapping("/workers")
    public String getWorkers(Model model, Principal principal) {

        try{
            model.addAttribute("workers", service.getAllWorkers());

            final UserDetails user = workerService.loadUserByUsername(principal.getName());

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
        Worker worker = service.getById(id);
        model.addAttribute("worker", worker);
        return "Director/edit_worker";
    }

    //на случай сброса базы - добавляет сотрудника
    @GetMapping("/supersecretrequest7355")
    public String addAdmin(Model model) {
        Worker worker = new Worker("admin", "admin", "admin", "a@b.c", "991122334455", "address", "директор", "password");
        worker.setRoles("ROLE_ADMIN");
        worker.setPassword(new BCryptPasswordEncoder().encode("74553211"));
        service.saveWorker(worker);

        return "redirect:/entrance";
    }


    @GetMapping("/worker_remove")
    public String removeWorker(@RequestParam Long id) {
        if (!service.existsWithId(id))
            throw new NoSuchElementException();
        service.removeById(id);
        return "redirect:/workers";
    }

    @GetMapping("/add_worker")
    public String addWorker(Model model) {
        model.addAttribute("worker", new Worker());
        return "Director/add_worker";
    }

    @PostMapping("/worker_update")
    public String editingSubmit(@Valid Worker worker, BindingResult result) {
        if (result.hasErrors()) {
            return "Director/edit_worker";
        }

        //если пароль не поменяли, то хешировать снова не стоит, если поменяли, то хешируем
        if(!worker.getPassword().equals(service.getById(worker.getId()).getPassword())) {
            worker.setPassword(new BCryptPasswordEncoder().encode(worker.getPassword()));
        }
        service.saveWorker(worker);
        return "redirect:/workers";
    }

    @PostMapping("/add_worker")
    public String greetingSubmit(@Valid Worker worker, BindingResult result) {
        if (result.hasErrors()) {
            return "Director/add_worker";
        }
        switch(worker.getPosition().toLowerCase()) {
            case "повар": worker.setRoles("ROLE_COOK"); break;
            case "официант": worker.setRoles("ROLE_WAITER"); break;
            case "директор": worker.setRoles("ROLE_ADMIN"); break;
        }
        worker.setPassword(new BCryptPasswordEncoder().encode(worker.getPassword()));
        service.saveWorker(worker);
        return "redirect:/workers";
    }
}
