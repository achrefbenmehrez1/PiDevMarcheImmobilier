package tn.esprit.realestate.Services.Forum;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.realestate.Entities.Forum.Comment;
import tn.esprit.realestate.Entities.Forum.Reply;
import tn.esprit.realestate.IServices.Forum.IReplyService;
import tn.esprit.realestate.Repositories.Forum.CommentRepository;
import tn.esprit.realestate.Repositories.Forum.ReplyRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReplyService implements IReplyService {

    @Autowired
    private ReplyRepository replyRepository;
    @Autowired
    private CommentRepository commentRepository;

    public List<Reply> getAllReplies() {
        return replyRepository.findAll();
    }

    public Reply getReplyById(Long id) {
        Optional<Reply> reply = replyRepository.findById(id);
        if (reply.isPresent()) {
            return reply.get();
        } else {
            return null;
        }
    }

    public Reply createReply(Reply reply) {
        reply.setCreatedAt(LocalDateTime.now());
        return replyRepository.save(reply);
    }

    public Reply updateReply(Long id, Reply reply) {
        Reply existingReply = getReplyById(id);
        existingReply.setContent(reply.getContent());
        return replyRepository.save(existingReply);
    }

    public void deleteReply(Long id) {
        Reply reply = getReplyById(id);
        replyRepository.delete(reply);
    }

    @Override
    public List<Reply> getRepliesByAuthor(Long authorId) {
        return replyRepository.findByAuthor_Id(authorId);
    }

    @Override
    public List<Reply> getRepliesByComment(Long commentId) {
        return replyRepository.findByComment_Id(commentId);
    }

    @Override
    public List<Reply> getRepliesByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return replyRepository.findByCreatedAtBetween(startDate, endDate);
    }

    @Override
    public List<Reply> getLatestReplies(int count) {
        return replyRepository.findTopNByOrderByCreatedAtDesc(count);
    }

    public List<Reply> getRepliesWithMostReactions() {
        return replyRepository.findAllOrderByReactionsDesc();
    }
}