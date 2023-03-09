package tn.esprit.realestate.IServices;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.lang.NonNull;
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
    User createUser(String email, String password, Role role, String username, String address, String phone, MultipartFile profileImage) throws IOException;

    User updateUser(Long id ,Optional<String> email, Optional<String> password,Optional<Role> role, Optional<String> username, Optional<String> address, Optional<String> phone, Optional<MultipartFile > profileImage) throws IOException;

    public void deleteUser(Long id);

    User getUserByToken(@NonNull HttpServletRequest request);

    User updateUserByToken(@NonNull HttpServletRequest request ,Optional<String> email,Optional<String>password,Optional<String>username, Optional<String> address, Optional<String> phone, Optional<MultipartFile> profileImage) throws IOException;

    List<User> getusers(Role role, String email, String username, String lastname, String address, String phone);
}
