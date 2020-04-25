package ua.opu.kurs_gorbik_kozyrevych.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.opu.kurs_gorbik_kozyrevych.Dish;
import ua.opu.kurs_gorbik_kozyrevych.MyUserDetails;
import ua.opu.kurs_gorbik_kozyrevych.Worker;
import ua.opu.kurs_gorbik_kozyrevych.repositories.WorkerRepository;

import java.util.List;
import java.util.Optional;

@Service
public class WorkerService implements UserDetailsService {

    private WorkerRepository repository;

    @Autowired
    public void setRepository(WorkerRepository repository) {
        this.repository = repository;
    }

    public void saveWorker(Worker worker) {
        repository.save(worker);
    }

    public List<Worker> getAllWorkers() {
        return repository.findAll();
    }

    public Worker getById(long id) {
        return repository.getOne(id);
    }

    public void removeById(long id) {
        repository.deleteById(id);
    }

    public boolean existsWithId(long id) {
        return repository.existsById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Worker> user = repository.findByUsername(username);
        System.out.println(user);
        user.orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        return user.map(MyUserDetails::new).get();
    }
}
