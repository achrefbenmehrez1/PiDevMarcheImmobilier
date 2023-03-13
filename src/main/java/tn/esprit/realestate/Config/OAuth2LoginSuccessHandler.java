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
import tn.esprit.realestate.Repositories.TokenRepository;
import tn.esprit.realestate.Repositories.UserRepository;

import java.io.IOException;

@Component
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    @Autowired
    private UserRepository userService;

    @Autowired
    private JwtService jwtService;
    @Autowired
    private TokenRepository tokenRepository;
  @Autowired
  private CostumerOAuth2UserService costumerOAuth2UserService;

    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        CustomerOauth2User oauth2User=(CustomerOauth2User)  authentication.getPrincipal();
        String email = oauth2User.getEmail();
        String name = oauth2User.getName();
        System.out.println("OAuth2 Username: "+name);
        System.out.println("OAuth2 Mail: "+email);
        User user= userService.findByEmail(email).orElse(null);
        userService.findByEmail(getDefaultTargetUrl());
        if(user==null){
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setUsername(name);
            newUser.setRole(Role.USER);

            user= newUser;
            ;
            userService.save(user);
            System.out.println("adding user to database");

        }else {
            System.out.println("User already exists");
        }


        var jwtToken = jwtService.generateToken(user);
costumerOAuth2UserService.revokeAllUserTokens(user);
costumerOAuth2UserService.saveUserToken(user,jwtToken);

        super.onAuthenticationSuccess(request, response, authentication);
    }

}
