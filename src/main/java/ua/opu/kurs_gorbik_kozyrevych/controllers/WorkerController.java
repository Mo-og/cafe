package ua.opu.kurs_gorbik_kozyrevych.controllers;

import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.NoSuchElementException;
import java.util.regex.Pattern;


@Controller
public class WorkerController {

    private WorkerService service;

    @Autowired
    public void setService(WorkerService service) {
        this.service = service;
    }

    @GetMapping("/Director/workers")
    public String getWorkers(Model model) {
        model.addAttribute("workers", service.getAllWorkers());
        return "Director/workers";
    }

    @GetMapping("/Waiter/workers")
    public String getWorkersWaiter(Model model) {
        model.addAttribute("workers", service.getAllWorkers());
        return "Waiter/workers";
    }

    @GetMapping("/Director/worker_edit")
    public String editWorker(Model model, @RequestParam Long id) {
        Worker worker = service.getById(id);
        model.addAttribute("worker", worker);
        return "Director/edit_worker";
    }

    @GetMapping("/Director/worker_remove")
    public String removeWorker(@RequestParam Long id) {
        if (!service.existsWithId(id))
            throw new NoSuchElementException();
        service.removeById(id);
        return "redirect:/Director/workers";
    }

    @GetMapping("/Director/add_worker")
    public String addWorker(Model model) {
        model.addAttribute("worker", new Worker());
        return "Director/add_worker";
    }

    @PostMapping("/Director/worker_update")
    public String editingSubmit(@Valid Worker worker, BindingResult result) {
        if (result.hasErrors()) {
            return "Director/edit_worker";
        }

        //если пароль не поменяли, то хешировать снова не стоит, если поменяли, то хешируем
        if(!worker.getPassword().equals(service.getById(worker.getId()).getPassword())) {
            worker.setPassword(new BCryptPasswordEncoder().encode(worker.getPassword()));
        }
        service.saveWorker(worker);
        return "redirect:/Director/workers";
    }

    @PostMapping("/Director/add_worker")
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
        return "redirect:/Director/workers";
    }
}
