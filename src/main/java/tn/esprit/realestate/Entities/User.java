package tn.esprit.realestate.Entities;


        import java.util.List;

        import com.fasterxml.jackson.annotation.JsonIgnore;
        import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
        import jakarta.persistence.*;

        import java.util.Collection;

        import lombok.*;
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
@ToString
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
    @JsonIgnore
    @Column
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @JsonIgnore
    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    private List<Token> tokens;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Agency agency;

    @OneToMany(mappedBy ="user",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Advertisement> advertisements;
}
