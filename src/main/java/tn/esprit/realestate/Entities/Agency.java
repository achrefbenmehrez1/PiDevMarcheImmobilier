package tn.esprit.realestate.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
@Getter
@Setter
public class Agency {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String address;

    private String email;

    private String phone;

    private String description;

    private String logo;

    @OneToMany(mappedBy = "agency", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<User> agents;
}
