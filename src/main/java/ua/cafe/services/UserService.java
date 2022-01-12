package ua.cafe.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.cafe.entities.User;
import ua.cafe.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private UserRepository repository;

    @Autowired
    public void setRepository(UserRepository repository) {
        this.repository = repository;
    }

    public void saveUser(User user) {
        repository.save(user);
    }

    public List<User> getAllUsers() {
        return repository.findAll();
    }

    public User getById(long id) {
        return repository.getOne(id);
    }

    public void removeById(long id) {
        repository.deleteById(id);
    }

    public void removeByUsername(String username) {
        Optional<User> userOptional = repository.findByUsername(username);
        userOptional.ifPresent(user -> repository.delete(user));
    }

    public boolean existsWithId(long id) {
        return repository.existsById(id);
    }

    public boolean existsWithUsername(String username) {
        return repository.findByUsername(username).isPresent();
    }

    public User getByUsername(String username) {
        Optional<User> user = repository.findByUsername(username);
        return user.orElse(null);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = repository.findByUsername(username);
        user.orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        return user.get();
    }
}
