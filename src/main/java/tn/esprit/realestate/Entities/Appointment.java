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
@Slf4j
@Getter
@Setter
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = true)
    private String location;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentType appointmentType;

    @Column(nullable = true)
    private String meetingLink;

    @ManyToOne
    private User agent;

    @ManyToOne
    private User client;
}
