package tn.esprit.realestate.Controllers;

import com.stripe.exception.StripeException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.realestate.Entities.User;
import tn.esprit.realestate.Services.User.StripeService;
import tn.esprit.realestate.Services.User.UserService;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/account")
public class AccountController {
    private final UserService userService;

    private final StripeService stripeService;
    public AccountController(UserService userService, StripeService stripeService) {
        this.userService = userService;
        this.stripeService = stripeService;
    }

    @GetMapping("/profile")
    public User getProfile(@NonNull HttpServletRequest request) {
        return userService.getUserByToken(request);
    }
    @PutMapping(value="/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public User updateProfile(@NonNull HttpServletRequest request, @RequestParam Optional<String> email, @RequestParam Optional <String> password, @RequestParam Optional <String> username, @RequestParam Optional <String> address, @RequestParam Optional <String> phone, @RequestParam Optional <MultipartFile> profileImage) throws IOException {
        return userService.updateUserByToken(request, email, password, username, address,phone, profileImage);

    }
    @PostMapping(value="/premium",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public User upgradeToPremium(@NonNull HttpServletRequest request,@RequestParam String number,@RequestParam Integer expMonth,@RequestParam Integer expYear,@RequestParam String cvc) throws StripeException {
        stripeService.upgradeToPremium(request,number,expMonth,expYear,cvc);
        return userService.getUserByToken(request);
    }


}