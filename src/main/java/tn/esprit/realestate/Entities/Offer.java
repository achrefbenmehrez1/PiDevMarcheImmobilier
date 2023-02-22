package tn.esprit.realestate.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    private String price;

    @ManyToOne
    @JsonIgnore
    private Property property;

    @ManyToOne
    @JsonIgnore
    private User user;
}
