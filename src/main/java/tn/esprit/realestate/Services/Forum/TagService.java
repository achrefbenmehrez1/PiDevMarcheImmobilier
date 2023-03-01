package tn.esprit.realestate.Services.Forum;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.realestate.Entities.Forum.Post;
import tn.esprit.realestate.Entities.Forum.Tag;
import tn.esprit.realestate.IServices.Forum.ITagService;
import tn.esprit.realestate.Repositories.Forum.PostRepository;
import tn.esprit.realestate.Repositories.Forum.TagRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TagService implements ITagService {

    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private PostRepository postRepository;

    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }

    public Tag getTagById(Long id) {
        Optional<Tag> tag = tagRepository.findById(id);
        if (tag.isPresent()) {
            return tag.get();
        } else {
            return null;
        }
    }

    public Tag createTag(Tag tag) {
        tag.setCreatedAt(LocalDateTime.now());
        return tagRepository.save(tag);
    }

    public Tag updateTag(Long id, Tag tag) {
        Tag existingTag = getTagById(id);
        existingTag.setName(tag.getName());
        return tagRepository.save(existingTag);
    }

    public void deleteTag(Long id) {
        Tag tag = getTagById(id);
        tagRepository.delete(tag);
    }

    @Override
    public List<Post> getPostsByTag(String tag) {
        return postRepository.findByTags_Name(tag);
    }

    @Override
    public List<Tag> getTagsByPost(Long postId) {
        return tagRepository.findByPosts_Id(postId);
    }

    @Override
    public List<Tag> getTagsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return tagRepository.findByCreatedAtBetween(startDate, endDate);
    }

    @Override
    public List<Object[]> getMostUsedTags(int count) {
        return tagRepository.findMostUsedTags(count);
    }
}

