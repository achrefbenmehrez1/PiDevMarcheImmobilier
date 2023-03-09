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
public class OfferParticipate {
    @Id
    @GeneratedValue(strategy =

            GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "offer_id")
    private Offer offer;
    @ManyToOne
    @JoinColumn(name = "client_id")
    private User user;
    private String description;

}