package tn.esprit.realestate.Controllers;

import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tn.esprit.realestate.Entities.Role;
import tn.esprit.realestate.Entities.User;
import tn.esprit.realestate.Services.User.UserService;

import java.io.IOException;
import java.util.List;
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(value="/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)

    public ResponseEntity<User> createUser(@Valid @RequestParam String email, @RequestParam String password, @RequestParam Role role, @RequestParam(required = false) String username, @RequestParam(required = false) String address, @RequestParam(required = false) String phone, @RequestParam(required = false,value="file") MultipartFile profileImage) throws IOException {
        User createdUser = userService.createUser(email,password,role,username,address,phone,profileImage);
        return ResponseEntity.created(
                        ServletUriComponentsBuilder.fromCurrentRequest()
                                .path("/{id}")
                                .buildAndExpand(createdUser.getId())
                                .toUri())
                .body(createdUser);
    }

    @PutMapping(value="/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<User> updateUser(@Valid @PathVariable Long id, @RequestParam(required = false) String email, @RequestParam(required = false) String password, @RequestParam(required = false) Role role, @RequestParam(required = false) String username,@RequestParam(required = false) String address,@RequestParam(required = false) String phone,@RequestParam(required = false) MultipartFile profileImage) throws IOException {

        User updatedUser = userService.updateUser(id,email,password,role,username,address,phone,profileImage);

        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
    @PostMapping(value ="/store", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String storeProfileImage(@RequestParam MultipartFile profileImage) throws IOException {
        return profileImage.getOriginalFilename();
    }
    @GetMapping("/search")
    public List<User> getusers(@RequestParam(value="role",required = false) Role role,
                                      @RequestParam(value="email",required = false) String email,
                                      @RequestParam(value="username",required = false) String username,
                                      @RequestParam(value="lastname",required = false) String lastname,
                                      @RequestParam(value="address",required = false) String address,
                                      @RequestParam(value="phone",required = false) String phone){

        return userService.getusers(role,email,username,lastname,address,phone);
    }
}

