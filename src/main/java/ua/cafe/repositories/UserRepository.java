package ua.cafe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.cafe.models.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
