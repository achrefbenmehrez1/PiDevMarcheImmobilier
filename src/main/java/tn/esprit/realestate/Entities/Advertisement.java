package tn.esprit.realestate.Entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Slf4j
@Getter
@Setter
public class Advertisement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String title;
    @Column
    private String description;
    @Column
    @Enumerated
    private TypeAd typeAd;
    @ManyToOne
    private Property property;
    @ManyToOne
    private AppUser user;
    @OneToOne
    private Agreement agreement;
}
