package FulfilmentService.repositories;

import FulfilmentService.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

    boolean isPresentByUsername(String s);
}
