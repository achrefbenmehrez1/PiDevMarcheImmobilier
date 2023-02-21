package tn.esprit.realestate.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.realestate.Entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
}