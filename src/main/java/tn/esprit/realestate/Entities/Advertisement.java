package tn.esprit.realestate.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Advertisement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NonNull
    private String title;

    @Column
    private Double price;

    @Transient
    private double oldPrice;


    @Column(length = 5000)
    @Lob
    private String description;


    @NonNull
    @Enumerated(EnumType.STRING)
    private TypeAd typeAd;

    @Column(length = 5000)
    @Lob
    private String foreignAdUrl;

    @Column
    private boolean scraped;

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

    public Advertisement(String title,Double price, String description, TypeAd typeAd,String foreignAdUrl,boolean scraped) {
        this.title=title;
        this.price=price;
        this.description=description;
        this.typeAd=typeAd;
        this.foreignAdUrl=foreignAdUrl;
        this.scraped=scraped;

    }


}