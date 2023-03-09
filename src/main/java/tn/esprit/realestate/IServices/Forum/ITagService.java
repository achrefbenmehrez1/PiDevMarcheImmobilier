package tn.esprit.realestate.IServices.Forum;

import tn.esprit.realestate.Entities.Forum.Post;
import tn.esprit.realestate.Entities.Forum.Tag;

import java.time.LocalDateTime;
import java.util.List;

public interface ITagService {
    public List<Tag> getAllTags();

    public Tag getTagById(Long id);

    public Tag createTag(Tag tag);

    public Tag updateTag(Long id, Tag tag);

    public void deleteTag(Long id);

    public List<Post> getPostsByTag(String tag);

    public List<Tag> getTagsByPost(Long postId);

    public List<Tag> getTagsByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    public List<Object[]> getMostUsedTags(int count);
}
