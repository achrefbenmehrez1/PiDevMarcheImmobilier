package tn.esprit.realestate.Entities;


        import java.util.List;

        import jakarta.persistence.*;

        import java.util.Collection;

        import lombok.AllArgsConstructor;
        import lombok.Builder;
        import lombok.Data;
        import lombok.NoArgsConstructor;
        import org.springframework.security.core.GrantedAuthority;
        import org.springframework.security.core.authority.SimpleGrantedAuthority;
        import org.springframework.security.core.userdetails.UserDetails;
        import org.springframework.web.multipart.MultipartFile;
        import tn.esprit.realestate.Security.Token;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String firstname;
    @Column(nullable = false)
    private String lastname;
    @Column(nullable = true)
    private String address;

    @Column(nullable = true)
    private String phone;


    @Transient
    private MultipartFile profileImage;
    @Column
    private String profileImagePath;
    @Column(unique=true)
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user")
    private List<Token> tokens;

    @ManyToOne
    private Agency agency;

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
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}