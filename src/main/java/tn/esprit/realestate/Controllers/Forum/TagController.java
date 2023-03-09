package tn.esprit.realestate.Controllers.Forum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import tn.esprit.realestate.Entities.Forum.Post;
import tn.esprit.realestate.Entities.Forum.Tag;
import tn.esprit.realestate.Services.Forum.TagService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/tags")
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping
    public List<Tag> getAllTags() {
        return tagService.getAllTags();
    }

    @GetMapping("/{id}")
    public Tag getTagById(@PathVariable Long id) {
        return tagService.getTagById(id);
    }

    @PostMapping
    public Tag createTag(@RequestBody Tag tag) {
        return tagService.createTag(tag);
    }

    @PutMapping("/{id}")
    public Tag updateTag(@PathVariable Long id, @RequestBody Tag tag) {
        return tagService.updateTag(id, tag);
    }

    @DeleteMapping("/{id}")
    public void deleteTag(@PathVariable Long id) {
        tagService.deleteTag(id);
    }

    @GetMapping(params = {"tag"})
    public List<Post> getPostsByTag(@RequestParam("tag") String tag) {
        return tagService.getPostsByTag(tag);
    }

    @GetMapping("/{id}/posts")
    public List<Tag> getTagsByPost(@PathVariable Long id) {
        return tagService.getTagsByPost(id);
    }

    @GetMapping(params = {"startDate", "endDate"})
    public List<Tag> getTagsByDateRange(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return tagService.getTagsByDateRange(startDate, endDate);
    }

    @GetMapping(params = {"count"})
    public List<Object[]> getMostUsedTags(@RequestParam("count") int count) {
        return tagService.getMostUsedTags(count);
    }
}
