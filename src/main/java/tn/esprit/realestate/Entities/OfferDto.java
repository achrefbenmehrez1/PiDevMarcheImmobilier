package tn.esprit.realestate.Entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Entity
@Getter
@Setter
public class OfferDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;


    private Boolean participate = false;
   /* @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Property property;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User user;
    @OneToOne(mappedBy = "offer", cascade = CascadeType.ALL)
    @JsonIgnoreProperties({"offer", "details"})
    private Details details;
    @OneToMany(mappedBy = "offer")
    private List<OfferParticipate> offerParticipates;*/







}
