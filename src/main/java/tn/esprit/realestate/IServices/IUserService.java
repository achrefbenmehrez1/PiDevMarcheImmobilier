package tn.esprit.realestate.IServices;

import tn.esprit.realestate.Entities.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    public List<User> getAllUsers();

   public Optional<User> getUserById(Long id);

    public Optional<User> getUserByEmail(String email);

    public User createUser(User user);

    public User updateUser(User user);

    public void deleteUser(Long id);
}
