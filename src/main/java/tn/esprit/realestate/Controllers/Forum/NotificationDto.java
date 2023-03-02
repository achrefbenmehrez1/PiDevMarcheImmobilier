package tn.esprit.realestate.Controllers.Forum;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A DTO for the {@link tn.esprit.realestate.Entities.Forum.Notification} entity
 */
@Data
public class NotificationDto implements Serializable {
    private final String message;
    private final Boolean readByte;
    private final String userFirstname;
    private final String userLastname;
    private final String userUsername;
    private final String userEmail;
    private final LocalDateTime createdAt;
}