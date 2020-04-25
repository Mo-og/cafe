package ua.opu.kurs_gorbik_kozyrevych.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.opu.kurs_gorbik_kozyrevych.Worker;

import java.util.Optional;

public interface WorkerRepository  extends JpaRepository<Worker,Long> {
    Optional<Worker> findByUsername(String username);
}
