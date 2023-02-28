package tn.esprit.realestate.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Advertisement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NonNull
    private String title;

    @Column
    private Double price;

    @Column
    private String description;


    @NonNull
    @Enumerated(EnumType.STRING)
    private TypeAd typeAd;

    @ManyToOne
    @JsonIgnore
    private User user;

    @OneToOne
    private Property property;


    public Advertisement(String title,Double price, String description, TypeAd typeAd) {
        this.title=title;
        this.price=price;
        this.description=description;
        this.typeAd=typeAd;

    }


}