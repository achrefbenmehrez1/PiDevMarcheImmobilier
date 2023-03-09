package tn.esprit.realestate.Dto.Forum;

import lombok.Data;
import tn.esprit.realestate.Entities.Forum.Tag;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * A DTO for the {@link tn.esprit.realestate.Entities.Forum.Post} entity
 */
@Data
public class PostDto implements Serializable {
    private final Long id;
    private final String title;
    private final String content;
    private final LocalDateTime createdAt;
    private final Long authorId;
    private final String authorUsername;
    private final List<Tag> tags;

    /**
     * A DTO for the {@link tn.esprit.realestate.Entities.Forum.Comment} entity
     */
    @Data
    public static class CommentDto implements Serializable {
        private final Long id;
        private final String content;
        private final Long authorId;
        private final String authorFirstname;
        private final String authorLastname;
        private final String authorUsername;
        private final String authorEmail;
        private final String authorPassword;
        private final LocalDateTime createdAt;
        private final LocalDateTime updatedAt;
    }

    /**
     * A DTO for the {@link tn.esprit.realestate.Entities.Forum.Reaction} entity
     */
    @Data
    public static class ReactionDto implements Serializable {
        private final Long id;
        private final String type;
        private final LocalDateTime createdAt;
    }

    /**
     * A DTO for the {@link tn.esprit.realestate.Entities.Forum.Tag} entity
     */
    @Data
    public static class TagDto implements Serializable {
        private final Long id;
        private final String name;
        private final LocalDateTime createdAt;
    }
}