package tn.esprit.realestate.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.realestate.Entities.AppUser;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    AppUser findByUsername(String username);
}