package tn.esprit.realestate.Entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.realestate.Security.Token;

import java.util.Collection;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
@ToString
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String username;

    @Column(nullable = true)
    private String address;

    @Column(unique = true,nullable = true)
    private String phone;


    @Transient
    private MultipartFile profileImage;
    @Column
    private String profileImagePath;
    @Column(unique=true)
    private String email;
    @JsonIgnore
    @Column
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;


    @Column(nullable = false)
    private boolean premium = false;

    @JsonIgnore
    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    private List<Token> tokens;


    @OneToMany(mappedBy ="user",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Advertisement> advertisements;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<FavoriteAd> favorites ;

    public User(String email, String password, Role role, String username, String address, String phone) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.username = username;

        this.address = address;
        this.phone = phone;
    }

    public User(String username, String email, Role user) {
        this.username = username;
        this.email = email;
        this.role = user;
    }

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @JsonIgnore
    @Override

    public boolean isAccountNonExpired() {
        return true;
    }
    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }


}
