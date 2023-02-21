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
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String description;

    private String price;

    @ManyToOne
    private Property property;

    @ManyToOne
    private User user;
}
