package FulfilmentService.repositories;

import FulfilmentService.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    boolean isPresentByUsername(String s);
    Optional<User> findByUsername(String username);
}
