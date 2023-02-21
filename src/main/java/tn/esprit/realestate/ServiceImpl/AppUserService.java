package tn.esprit.realestate.ServiceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import tn.esprit.realestate.Entities.AppUser;
import tn.esprit.realestate.Entities.Role;
import tn.esprit.realestate.Repositories.AppUserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AppUserService implements UserDetailsService {
    private final AppUserRepository appUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws
            UsernameNotFoundException {
        AppUser appUser = appUserRepository.findByUsername(username);
        Assert.notNull(appUser,
                new UsernameNotFoundException(username).getMessage());
        return new User(appUser.getUsername()
                , appUser.getPassword()
                , getAuthorities(Objects.requireNonNull(appUser.getRoles().stream().findFirst().orElse(null))));
    }

    private List<GrantedAuthority> getAuthorities(Role userRole) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(userRole.getName()));
        return authorities;
    }
}