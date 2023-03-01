package tn.esprit.realestate.Controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.realestate.Config.JwtService;
import tn.esprit.realestate.Entities.Role;
import tn.esprit.realestate.Entities.User;
import tn.esprit.realestate.Services.User.UserService;

import java.io.IOException;

@RestController
@RequestMapping("/account")
public class AccountController {
    private final UserService userService;


    public AccountController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public User getProfile(@NonNull HttpServletRequest request) {
        return userService.getUserByToken(request);
    }
    @PutMapping("/update")
    public User updateProfile(@NonNull HttpServletRequest request, @RequestParam(required = false) String email, @RequestParam(required = false) String password, @RequestParam(required = false) String firstname, @RequestParam(required = false) String lastname, @RequestParam(required = false) String address, @RequestParam(required = false) String phone, @RequestParam(required = false) MultipartFile profileImage) throws IOException {
     return userService.updateUserByToken(request, email, password, firstname, lastname, address, phone, profileImage);

    }

}
