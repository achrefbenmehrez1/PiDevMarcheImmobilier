package tn.esprit.realestate.Entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Slf4j
@Getter
@Setter
public class Agreement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column private String payment_method;
    @Column private String penalties;
    @OneToOne private Advertisement advertisement;

    @ManyToOne
    private AppUser client;

    @ManyToOne
    private AppUser agent;

    @ManyToOne
    private AppUser owner;
}
