package realestate.pidev.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import realestate.pidev.Entities.User;

public interface UserRepository extends JpaRepository<User, Integer> {
}