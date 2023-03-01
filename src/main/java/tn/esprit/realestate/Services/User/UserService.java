package tn.esprit.realestate.Services.User;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import tn.esprit.realestate.Entities.Role;
import tn.esprit.realestate.Entities.User;
import tn.esprit.realestate.IServices.IUserService;
import tn.esprit.realestate.Repositories.UserRepository;

import java.nio.file.StandardCopyOption;
import java.util.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private  PasswordEncoder passwordEncoder;

    private  UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    @Override
    public String storeProfileImage(MultipartFile profileImage) throws IOException {
        String imagePath = null;
        if (profileImage != null && !profileImage.isEmpty()) {
            String fileName = StringUtils.cleanPath(profileImage.getOriginalFilename());
            String currentDir = System.getProperty("user.dir");
            Path uploadDir = Paths.get(currentDir, "src", "main", "resources", "user-profiles");
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }
            try (InputStream inputStream = profileImage.getInputStream()) {
                Path filePath = uploadDir.resolve(fileName);
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                imagePath = filePath.toAbsolutePath().toString();
            } catch (IOException ex) {
                throw new IOException("Could not store file " + fileName + ". Please try again!", ex);
            }
        }
        return imagePath;
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }



    @Override
    public User createUser(String email, String password, Role role, String firstname, String lastname, String address, String phone, MultipartFile profileImage) throws IOException {
        User user = new User(email, password, role, firstname, lastname, address, phone);
        String profileImagePath = storeProfileImage(profileImage);
        user.setProfileImagePath(profileImagePath);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long id, String email, String password, Role role, String firstname, String lastname, String address, String phone, MultipartFile profileImage) throws IOException {
        User user =getUserById(id).get();
        if (email != null) {
            user.setEmail(email);
        }
        if (password != null) {
            user.setPassword(password);

        }
        if (role != null) {
            user.setRole(role);
        }
        if (firstname != null) {
            user.setFirstname(firstname);
        }
        if (lastname != null) {
            user.setLastname(lastname);
        }
        if (address != null) {
            user.setAddress(address);
        }
        if (phone != null) {
            user.setPhone(phone);
        }


        String profileImagePath = storeProfileImage(profileImage);
        if (profileImagePath != null) {
            user.setProfileImagePath(profileImagePath);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }


    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}

