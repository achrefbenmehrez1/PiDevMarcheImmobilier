package tn.esprit.realestate.IServices;

import org.springframework.web.multipart.MultipartFile;
import tn.esprit.realestate.Entities.Role;
import tn.esprit.realestate.Entities.User;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface IUserService {
    public List<User> getAllUsers();

    String storeProfileImage(MultipartFile profileImage) throws IOException;

    public Optional<User> getUserById(Long id);

    public Optional<User> getUserByEmail(String email);
    User createUser(String email, String password, Role role, String firstname, String lastname, String address, String phone, MultipartFile profileImage) throws IOException;

    User updateUser(Long id, String email, String password, Role role, String firstname, String lastname, String address, String phone, MultipartFile profileImage) throws IOException;

    public void deleteUser(Long id);
}
