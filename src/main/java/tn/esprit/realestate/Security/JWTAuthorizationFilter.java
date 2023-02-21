package tn.esprit.realestate.Security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

import tn.esprit.realestate.ServiceImpl.AppUserService;
import java.io.IOException;
@Slf4j
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    private AppUserService userService;
    @Autowired
    public JWTAuthorizationFilter(AppUserService userService) {
        this.userService = userService;
    }
    JWTUtils jwtUtils = new JWTUtils();
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse
            response, FilterChain filterChain)

            throws ServletException, IOException {
         //JWTUtils jwtUtils=new JWTUtils();
        if(request.getMethod().equals("OPTIONS")){
            response.setStatus(HttpServletResponse.SC_OK);
        }
        else {
            String jwt = request.getHeader(SecurityConstants.HEADER_STRING);
            if (jwt == null || !jwt.startsWith(SecurityConstants.TOKEN_PREFIX)) {
                filterChain.doFilter(request, response);
                return;
            }
            this.validateToken(jwt);
            UserDetails userDetails =
                    userService.loadUserByUsername(jwtUtils.extractUsername(jwt));
//update the security context
            UsernamePasswordAuthenticationToken authenticatedUser;
            authenticatedUser = new UsernamePasswordAuthenticationToken(
                    userDetails.getUsername(),null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
            filterChain.doFilter(request, response);
        }
    }
    public Boolean validateToken(String token) {
        // JWTUtils jwtUtils=new JWTUtils();
        Assert.notNull(userService.loadUserByUsername(jwtUtils.extractUsername(token)),
                "User Doesn't exist");
        Assert.notNull(jwtUtils.isTokenExpired(token), "Token is Expired");
        return true;
    }
}