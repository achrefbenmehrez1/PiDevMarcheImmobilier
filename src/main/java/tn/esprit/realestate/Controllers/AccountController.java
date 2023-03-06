package tn.esprit.realestate.Controllers;

import com.stripe.exception.StripeException;
import com.stripe.model.Token;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.realestate.Entities.User;
import tn.esprit.realestate.Services.User.StripeService;
import tn.esprit.realestate.Services.User.UserService;

import java.io.IOException;

@RestController
@RequestMapping("/account")
public class AccountController {
    private final UserService userService;

private final StripeService stripeService;
    public AccountController(UserService userService,StripeService stripeService) {
        this.userService = userService;
        this.stripeService = stripeService;
    }

    @GetMapping("/profile")
    public User getProfile(@NonNull HttpServletRequest request) {
        return userService.getUserByToken(request);
    }
    @PutMapping("/update")
    public User updateProfile(@NonNull HttpServletRequest request, @RequestParam(required = false) String email, @RequestParam(required = false) String password, @RequestParam(required = false) String username, @RequestParam(required = false) String address, @RequestParam(required = false) String phone, @RequestParam(required = false) MultipartFile profileImage) throws IOException {
     return userService.updateUserByToken(request, email, password, username, address, phone, profileImage);

    }
    @PostMapping("/premium")
    public User upgradeToPremium(@NonNull HttpServletRequest request) throws StripeException {
        stripeService.upgradeToPremium(request);
        return userService.getUserByToken(request);
    }

}
