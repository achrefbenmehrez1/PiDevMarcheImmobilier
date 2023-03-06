package tn.esprit.realestate.Services.User;

import jakarta.persistence.criteria.Predicate;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import tn.esprit.realestate.Config.JwtService;
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
    private JwtService jwtService;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
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
    public User createUser(String email, String password, Role role, String username, String address, String phone, MultipartFile profileImage) throws IOException {
        User user = new User(email, password, role, username, address, phone);
        String profileImagePath = storeProfileImage(profileImage);
        user.setProfileImagePath(profileImagePath);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long id, String email, String password, Role role, String username, String address, String phone, MultipartFile profileImage) throws IOException {
        User user =getUserById(id).get();
        if (email != null) {
            user.setEmail(email);
        }
        if (password != null) {
            user.setPassword(passwordEncoder.encode(password));


        }
        if (role != null) {
            user.setRole(role);
        }
        if (username != null) {
            user.setUsername(username);
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

        return userRepository.save(user);
    }


    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
    @Override
    public User getUserByToken(@NonNull HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        jwt = authHeader.substring(7);
        userEmail = jwtService.extractUsername(jwt);

        return userRepository.findByEmailOrPhoneOrUsername(userEmail).get();
    }
    @Override
    public User updateUserByToken(@NonNull HttpServletRequest request, String email, String password,String username, String address, String phone, MultipartFile profileImage) throws IOException {
        User user = getUserByToken(request);
        if (email != null) {
            user.setEmail(email);
        }

            if (password != null) {
                user.setPassword(passwordEncoder.encode(password));


            }
            if (username != null) {
                user.setUsername(username);
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

        return userRepository.save(user);
    }
    @Override
    public List<User> getusers(Role role, String email, String username, String lastname,String address, String phone) {

        List<User> users=userRepository.findAll((Specification<User>) (root, cq, cb) -> {
            Predicate p = cb.conjunction();
            if( role!=null ){
                p= cb.and(p,cb.equal(root.get("role"),role));
            }
            if( email!=null  ){
                p=cb.and(p,cb.equal(root.get("email"),email));
            }

            if(username!=null ){
                p=cb.and(p,cb.like(root.get("username"),username));
            }

            if(lastname!=null ){
                p=cb.and(p,cb.equal(root.get("lastname"),lastname));
            }

            if(address!=null){
                p=cb.and(p,cb.equal(root.get("address"),address));
            }

            if(phone!=null){
                p=cb.and(p,cb.equal(root.get("phone"),phone));
            }

            cq.orderBy(cb.asc(root.get("id")));
            return p;
        });
        return users;
    }

    }


