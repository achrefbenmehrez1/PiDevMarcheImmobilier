package tn.esprit.realestate.Entities;

import jakarta.mail.Multipart;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
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
    private String ville;
    @Column
    private String region;

    @Column
    @ElementCollection
    private List<String> photo=new ArrayList<>();



    public Property(Double size, Type type, int rooms, boolean parking,
                    Double yardSpace, boolean garage,String ville,
                    String region,  List<String> photos)  {
        this.size=size;
        this.type=type;
        this.rooms=rooms;
        this.parking=parking;
        this.yardSpace=yardSpace;
        this.garage=garage;
        this.ville=ville;
        this.region=region;
        this.photo=photos;

    }

    public Property(Double size, Type type, int rooms,
                    boolean garage,
                    String region,String ville, List<String> photos) {
        this.size=size;
        this.type=type;
        this.rooms=rooms;
        this.garage=garage;
        this.region=region;
        this.ville=ville;
        this.photo=photos;

    }

}