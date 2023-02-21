package tn.esprit.realestate.Entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Entity
@Table
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Slf4j
@Getter
@Setter
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String message;

    private Date date;

    @ManyToOne
    private User sender;

    @ManyToOne
    private User receiver;
}
