package tn.esprit.realestate.Entities.Forum;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import tn.esprit.realestate.Entities.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "reactions")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Slf4j
@Getter
@Setter
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Reaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference(value = "post-reactions")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference(value = "comment-reactions")
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference(value = "reply-reactions")
    private Reply reply;

    @Column
    private String type;

    @Column
    private LocalDateTime createdAt;
}
