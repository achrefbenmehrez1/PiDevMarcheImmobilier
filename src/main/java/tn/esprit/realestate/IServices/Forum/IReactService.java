package tn.esprit.realestate.IServices.Forum;

import tn.esprit.realestate.Entities.Post;
import tn.esprit.realestate.Entities.React;
import tn.esprit.realestate.Entities.User;

import java.util.List;

public interface IReactService {
    public React createReact(React react);

    public React updateReact(Long reactId, React reactDetails);

    public void deleteReact(Long reactId);

    public List<React> getAllReacts();

    public React getReactById(Long reactId);

    public List<React> getAllReactsByPost(Post post);

    public List<React> getAllReactsByAuthor(User author);
}
