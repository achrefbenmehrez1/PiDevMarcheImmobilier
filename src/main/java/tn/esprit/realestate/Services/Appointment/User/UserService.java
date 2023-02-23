package tn.esprit.realestate.Services.Appointment.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.realestate.Entities.User;
import tn.esprit.realestate.IServices.IUserService;
import tn.esprit.realestate.Repositories.UserRepository;
import java.util.*;

@Service
public class UserService implements IUserService {

    private  UserRepository userRepository;
         @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
@Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
@Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
@Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
@Override
    public User createUser(User user) {
        return userRepository.save(user);
    }
@Override
    public User updateUser(User user) {
        return userRepository.save(user);
    }
@Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}

