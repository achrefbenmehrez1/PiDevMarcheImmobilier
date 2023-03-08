package tn.esprit.realestate.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tn.esprit.realestate.Entities.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {


    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);
    Optional<User> findByPhone(String phone);

    @Query("SELECT u FROM User u WHERE u.email = :emailOrPhoneOrUsername OR u.phone = :emailOrPhoneOrUsername OR u.username = :emailOrPhoneOrUsername")
    Optional<User> findByEmailOrPhoneOrUsername(String emailOrPhoneOrUsername);
}