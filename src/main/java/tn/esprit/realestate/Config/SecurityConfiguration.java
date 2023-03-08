package tn.esprit.realestate.Config;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import java.io.IOException;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor

public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;
    @Autowired
    private CostumerOAuth2UserService customerOauth2UserService;
    @Autowired
    private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .authorizeHttpRequests()
                .requestMatchers( "/auth/**","/OTP/**")
                .permitAll()
                .requestMatchers("/users/**")
                .hasAnyAuthority("ADMIN")
                .requestMatchers("/ad/getAdsNotPremium","/ad/searchNotScraped","/ad/getAdsByUsersLocation","/ad/getByid/{id}")
                .permitAll()
                .requestMatchers("/ad/**")
                .hasAnyAuthority("USER","PROMOTER")
                .requestMatchers("/account/**")
                .authenticated()
                .anyRequest()
                .authenticated()
                .and()


                //.formLogin()
                //.loginPage("/Accueil/Erreur")

                .oauth2Login()
                //.loginPage("/Accueil/Erreur")
                .userInfoEndpoint()
                .userService(customerOauth2UserService)
                .and()
                .successHandler(oAuth2LoginSuccessHandler)

                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()

                .exceptionHandling()
                .accessDeniedHandler(new CustomAccessDeniedHandler()) // gestionnaire pour l'erreur 403
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout()
                .logoutUrl("/auth/logout")
                .addLogoutHandler(logoutHandler)
                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext());

        return http.build();
    }
    private class CustomAccessDeniedHandler implements AccessDeniedHandler {
        @Override
        public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException exception) throws IOException, ServletException {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, exception.getMessage());
        }
    }

    // Classe privée pour gérer les erreurs d'authentification (401)
    private class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
        @Override
        public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
        }
    }

}


