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
    private double size;

    @Column
    @Enumerated(EnumType.STRING)
    private Type type;

    @Column
    private int rooms;

    @Column
    private boolean parking;

    @Column
    private double yardSpace;

    @Column
    private boolean garage;

    @Column
    private String region;
}