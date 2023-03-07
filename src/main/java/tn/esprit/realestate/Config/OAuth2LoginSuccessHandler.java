package tn.esprit.realestate.Config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import tn.esprit.realestate.Entities.Role;
import tn.esprit.realestate.Entities.User;
import tn.esprit.realestate.Repositories.UserRepository;
import tn.esprit.realestate.Services.User.UserService;

import java.io.IOException;
import java.util.Optional;

@Component
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    @Autowired
    private UserRepository userService;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        CustomerOauth2User oauth2User=(CustomerOauth2User)  authentication.getPrincipal();
        String email = oauth2User.getEmail();
        System.out.println("OAuth2 Username: "+oauth2User.getName());
        System.out.println("OAuth2 Mail: "+email);
        User user= userService.findByEmail(email).orElse(null);
        userService.findByEmail(getDefaultTargetUrl());
        if(user==null){
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setUsername(oauth2User.getName());
            newUser.setRole(Role.USER);
            ;
            userService.save(newUser);
             System.out.println("adding user to database");
        }else {
            System.out.println("User already exists");
        }
        super.onAuthenticationSuccess(request, response, authentication);
    }
    protected String determineTargetUrl(Authentication authentication) {
        // Determine target URL based on the user's authentication details
        // For example, you might check the user's authorities to see if they have access to a certain page
        return "{baseUrl}/Accueil";
    }
}
