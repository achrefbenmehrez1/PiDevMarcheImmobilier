package tn.esprit.realestate.IServices;

import org.springframework.web.multipart.MultipartFile;
import tn.esprit.realestate.Entities.User;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface IUserService {
    public List<User> getAllUsers();

    String storeProfileImage(MultipartFile profileImage) throws IOException;

    public Optional<User> getUserById(Long id);

    public Optional<User> getUserByEmail(String email);

    String uploadFile(MultipartFile file) throws IOException;

    User createUser(User user, MultipartFile profileImage) throws IOException;



    User updateUser(User user, MultipartFile image) throws IOException;

    public void deleteUser(Long id);
}
