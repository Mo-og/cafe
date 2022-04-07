package ua.cafe.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import ua.cafe.models.Authority;
import ua.cafe.models.LoginForm;
import ua.cafe.models.User;
import ua.cafe.repositories.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService implements UserDetailsService {

    private UserRepository repository;

    @Autowired
    public void setRepository(UserRepository repository) {
        this.repository = repository;
    }

    public String checkSaveUser(User user, BindingResult result, Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null)
            return "redirect:/login";

        User auth = (User) authentication.getPrincipal();
        if (auth.getPosition() != Authority.DIRECTOR) {
            //if user is not admin they change their own account
            //and thus cannot change their role

            user.setPosition(auth.getPosition());
        }
        Optional<User> fromDbOpt = repository.findById(user.getId());

        if (result.hasErrors()) {
            if (fromDbOpt.isEmpty()) return "edit_user";
            for (FieldError fieldError : result.getFieldErrors()) {
                if (user.getPassword().equals("") && fieldError.getField().equals("password"))
                    continue;
                return "edit_user";
            }
        }

        if (fromDbOpt.isPresent()) {
            User fromDb = fromDbOpt.get();
            //if password was not changed - nothing to encrypt
            if (user.getPassword().equals("")) {
                user.setPassword(fromDb.getPassword());
            } else user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        }
        user.setUsername(user.getUsername().replace("+", ""));
        repository.save(user);
        return "redirect:/users";
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

    public void checkLogin(Model model, LoginForm form, BindingResult result) {
        model.addAttribute("form", form);
        result.getFieldErrors().forEach(fieldError -> log.info(fieldError.getField() + "; " + fieldError.getDefaultMessage()));
        if (result.hasFieldErrors("username")) {
            model.addAttribute("messageUsername", "Некорректный номер телефона!");
            model.addAttribute("usernameFailed", true);
            if (result.hasFieldErrors("password")) {
                model.addAttribute("messagePassword", "Недопустимый пароль!");
                model.addAttribute("passwordFailed", true);
            }
            return;
        }
        if (result.hasFieldErrors("password")) {
            model.addAttribute("messagePassword", "Недопустимый пароль!");
            model.addAttribute("passwordFailed", true);
            if (!existsWithUsername(form.getUsername())) {
                model.addAttribute("messageUsername", "Пользователь не найден");
                model.addAttribute("usernameFailed", true);
            }
            return;
        }
        if (existsWithUsername(form.getUsername())) {
            model.addAttribute("messagePassword", "Введён неверный пароль!");
            model.addAttribute("passwordFailed", true);
        } else {
            model.addAttribute("messageUsername", "Пользователь не найден");
            model.addAttribute("usernameFailed", true);
        }
    }

    public void createAdmin() {
        removeByUsername("991122334455");
        saveUser(new User("admin", "admin", "admin", "a@b.c",
                "991122334455", "address", Authority.DIRECTOR, new BCryptPasswordEncoder().encode("74553211")));
    }
}
