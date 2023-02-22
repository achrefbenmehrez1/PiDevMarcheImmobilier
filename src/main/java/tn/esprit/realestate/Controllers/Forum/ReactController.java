package tn.esprit.realestate.Controllers.Forum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.realestate.Entities.Post;
import tn.esprit.realestate.Entities.React;
import tn.esprit.realestate.Entities.User;
import tn.esprit.realestate.Services.Forum.ReactService;

import java.util.List;

@RestController
@RequestMapping("/reacts")
public class ReactController {

    private final ReactService reactService;

    @Autowired
    public ReactController(ReactService reactService) {
        this.reactService = reactService;
    }

    @GetMapping("")
    public List<React> getAllReacts() {
        return reactService.getAllReacts();
    }

    @GetMapping("/{reactId}")
    public React getReactById(@PathVariable Long reactId) {
        return reactService.getReactById(reactId);
    }

    @GetMapping("/post/{postId}")
    public List<React> getAllReactsByPost(@PathVariable Long postId) {
        Post post = new Post();
        post.setId(postId);
        return reactService.getAllReactsByPost(post);
    }

    @GetMapping("/author/{authorId}")
    public List<React> getAllReactsByAuthor(@PathVariable Long authorId) {
        User author = new User();
        author.setId(authorId);
        return reactService.getAllReactsByAuthor(author);
    }

    @PostMapping("")
    public ResponseEntity<?> createReact(@RequestBody React react) {
        React savedReact = reactService.createReact(react);
        return new ResponseEntity<>(savedReact, HttpStatus.CREATED);
    }

    @PutMapping("/{reactId}")
    public React updateReact(@PathVariable Long reactId, @RequestBody React reactDetails) {
        return reactService.updateReact(reactId, reactDetails);
    }

    @DeleteMapping("/{reactId}")
    public ResponseEntity<?> deleteReact(@PathVariable Long reactId) {
        reactService.deleteReact(reactId);
        return ResponseEntity.ok().build();
    }
}
