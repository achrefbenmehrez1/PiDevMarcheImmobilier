package tn.esprit.realestate.Entities;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private Double size;

    @Column
    @Enumerated(EnumType.STRING)
    private Type type;

    @Column
    private int rooms;

    @Column
    private boolean parking;

    @Column
    private Double yardSpace;

    @Column
    private boolean garage;

    @Column
    private String region;

    @Column
    private String photo;

    public Property(Double size, Type type, int rooms, boolean parking,
                    Double yardSpace, boolean garage,
                    String region, String photo) {
        this.size=size;
        this.type=type;
        this.rooms=rooms;
        this.parking=parking;
        this.yardSpace=yardSpace;
        this.garage=garage;
        this.region=region;
        this.photo=photo;

    }

}