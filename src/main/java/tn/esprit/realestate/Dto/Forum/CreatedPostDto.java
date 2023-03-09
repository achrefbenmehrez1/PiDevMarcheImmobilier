package tn.esprit.realestate.Dto.Forum;

import lombok.Data;
import tn.esprit.realestate.Entities.Forum.Comment;
import tn.esprit.realestate.Entities.Forum.Post;
import tn.esprit.realestate.Entities.Forum.Reaction;
import tn.esprit.realestate.Entities.Forum.Tag;
import tn.esprit.realestate.Entities.User;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * A DTO for the {@link Post} entity
 */
@Data
public class CreatedPostDto implements Serializable {
    private final Long id;
    private final String title;
    private final String content;
    private final User author;
    private final List<Comment> comments;
    private final List<Reaction> reactions;
    private final List<Tag> tags;
    private final int views;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
}