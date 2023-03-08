package tn.esprit.realestate.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

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

    @Column
    private LocalDateTime created_at;

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

    @Transient
    private String currency;

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
        this.created_at= LocalDateTime.now();

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