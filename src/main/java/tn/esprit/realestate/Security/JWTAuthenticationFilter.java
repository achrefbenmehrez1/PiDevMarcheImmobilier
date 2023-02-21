package tn.esprit.realestate.Security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import
        org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.Assert;
import tn.esprit.realestate.Entities.AppUser;

import java.io.IOException;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter{

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {

        super(authenticationManager);

    }
    JWTUtils jwtUtils = new JWTUtils();
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) {
        AppUser appUser = new AppUser();
        try {
            appUser = new ObjectMapper()
                    .readValue(request.getInputStream(), AppUser.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Assert.notNull(appUser.getUsername(),"user empty");
        return this.getAuthenticationManager().authenticate(
                new
                        UsernamePasswordAuthenticationToken(appUser.getUsername(),appUser.getPassword())
        );
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,

                                            FilterChain chain,

                                            Authentication authResult) throws IOException,
            ServletException {
        //JWTUtils jwtUtils = new JWTUtils();
        User springUser = (User) authResult.getPrincipal();
        String jwtToken= jwtUtils.generateToken(springUser.getUsername());
        response.addHeader(SecurityConstants.HEADER_STRING , jwtToken);
    }
}


