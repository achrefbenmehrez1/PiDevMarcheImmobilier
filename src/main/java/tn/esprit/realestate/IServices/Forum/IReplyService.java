package tn.esprit.realestate.IServices.Forum;

import tn.esprit.realestate.Entities.Forum.Comment;
import tn.esprit.realestate.Entities.Forum.Reply;

import java.time.LocalDateTime;
import java.util.List;

public interface IReplyService {

    public List<Reply> getAllReplies();

    public Reply getReplyById(Long id);

    public Reply createReply(Reply reply);

    public Reply updateReply(Long id, Reply reply);

    public void deleteReply(Long id);

    public List<Reply> getRepliesByAuthor(Long authorId);

    public List<Reply> getRepliesByComment(Long commentId);

    public List<Reply> getRepliesByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    public List<Reply> getLatestReplies(int count);

    public List<Reply> getRepliesWithMostReactions();
}