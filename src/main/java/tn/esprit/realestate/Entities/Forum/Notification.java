package tn.esprit.realestate.Entities.Forum;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import tn.esprit.realestate.Entities.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Slf4j
@Getter
@Setter
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column private String message;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    @Column private Boolean readln;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}
