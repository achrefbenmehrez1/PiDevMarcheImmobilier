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
            Path uploadDir = Paths.get("./user-profiles");
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
    public String uploadFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        String fileName = file.getOriginalFilename();
        String filePath = "path/to/uploaded/file/" + fileName;
        file.transferTo(new File(filePath));
        return filePath;
    }
    @Override
    public User createUser(User user, MultipartFile profileImage) throws IOException {
        String profileImagePath = storeProfileImage(profileImage);
        user.setProfileImagePath(profileImagePath);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User updateUser(User user, MultipartFile profileImage) throws IOException {
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

