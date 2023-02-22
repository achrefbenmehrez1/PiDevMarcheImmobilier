package tn.esprit.realestate.Services.Forum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import tn.esprit.realestate.Entities.Post;
import tn.esprit.realestate.Entities.React;
import tn.esprit.realestate.Entities.User;
import tn.esprit.realestate.Repositories.ReactRepository;

import java.util.Date;
import java.util.List;

@Service
public class ReactService {

    private final ReactRepository reactRepository;

    @Autowired
    public ReactService(ReactRepository reactRepository) {
        this.reactRepository = reactRepository;
    }

    public React createReact(React react) {
        react.setCreationDate(new Date());
        return reactRepository.save(react);
    }

    public React updateReact(Long reactId, React reactDetails) {
        React react = reactRepository.findById(reactId)
                .orElseThrow(() -> new ResourceNotFoundException("React"));
        react.setType(reactDetails.getType());
        return reactRepository.save(react);
    }

    public void deleteReact(Long reactId) {
        React react = reactRepository.findById(reactId)
                .orElseThrow(() -> new ResourceNotFoundException("React"));
        reactRepository.delete(react);
    }

    public List<React> getAllReacts() {
        return reactRepository.findAll();
    }

    public React getReactById(Long reactId) {
        return reactRepository.findById(reactId)
                .orElseThrow(() -> new ResourceNotFoundException("React"));
    }

    public List<React> getAllReactsByPost(Post post) {
        return reactRepository.findByPost(post);
    }

    public List<React> getAllReactsByAuthor(User author) {
        return reactRepository.findByAuthor(author);
    }
}
