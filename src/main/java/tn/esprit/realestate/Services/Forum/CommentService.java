package tn.esprit.realestate.Services.Forum;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.realestate.Entities.Forum.*;
import tn.esprit.realestate.Entities.User;
import tn.esprit.realestate.IServices.Forum.ICommentService;
import tn.esprit.realestate.Repositories.Forum.AttachmentRepository;
import tn.esprit.realestate.Repositories.Forum.CommentRepository;
import tn.esprit.realestate.Repositories.Forum.PostRepository;
import tn.esprit.realestate.Repositories.Forum.ReplyRepository;
import tn.esprit.realestate.Repositories.UserRepository;
import tn.esprit.realestate.Utils.FileUploadUtil;
import tn.esprit.realestate.Utils.ProfanityFilter;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class CommentService implements ICommentService {
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ReplyRepository replyRepository;
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private AttachmentRepository attachmentRepository;
    @Autowired
    private Environment env;

    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

    public Comment getCommentById(Long id) {
        Optional<Comment> comment = commentRepository.findById(id);
        if (comment.isPresent()) {
            return comment.get();
        } else {
            return null;
        }
    }

    public ResponseEntity<String> createComment(Optional<MultipartFile> file, String content, Long authorId)
            throws MessagingException {
        Comment comment = new Comment();
        comment.setContent(content);

        Post post = new Post();
        User author = userRepository.findById(authorId).orElse(null);
        if (author == null) {
            return ResponseEntity.badRequest().body("Author not found");
        }
        if (ProfanityFilter.isProfanity(content)) {
            post.setFlagged(true);
        }

        post.setCreatedAt(LocalDateTime.now());
        post.setAuthor(author);

        // Save the attachment if it exists
        if (file.isPresent()) {
            String filename = StringUtils.cleanPath(file.get().getOriginalFilename());
            Attachment attachment = new Attachment();
            attachment.setName(filename);
            attachment.setPost(post);
            attachment.setAttachmentType(file.get().getContentType());
            attachment.setCreatedAt(LocalDateTime.now());

            String uploadDir = "src/main/resources/static/attachments";
            String attachmentLink = uploadDir + filename;
            FileUploadUtil.saveFile(uploadDir, filename, file.get());

            attachment.setAttachmentLink(attachmentLink);
            attachmentRepository.save(attachment);

            post.setAttachment(attachment);
        }

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(env.getProperty("spring.mail.username"));
        helper.setTo(postRepository.findById(comment.getPost().getId()).get().getAuthor().getEmail());
        helper.setSubject("A new comment has been added to your post");
        helper.setText("A new comment has been added to your post on Vendor.", true);
        javaMailSender.send(message);

        comment = commentRepository.save(comment);
        if (comment.isFlagged()) {
            String to = "achrefpgm@gmail.com";
            String subject = "A new comment has been flagged as Profanity";
            String body = "A new comment with Id: " + comment.getId() + ", created by User "
                    + comment.getAuthor().getEmail() + " , has been flagged on Vendor as Profanity.";
            MimeMessage message2 = javaMailSender.createMimeMessage();
            MimeMessageHelper helper2 = new MimeMessageHelper(message2);
            helper2.setFrom("achref.benmehrez@esprit.tn");
            helper2.setTo(to);
            helper2.setSubject(subject);
            helper2.setText(body, true);
            javaMailSender.send(message2);

            return ResponseEntity.badRequest().body("Profanity is not allowed");
        } else {
            return ResponseEntity.ok().body("Comment created successfully");
        }
    }

    public boolean deleteComment(Long id) {
        Comment comment = getCommentById(id);
        if (comment != null) {
            commentRepository.delete(comment);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<Comment> getCommentsByAuthor(Long authorId) {
        return commentRepository.findByAuthor_Id(authorId);
    }

    @Override
    public List<Comment> getCommentsByPost(Long postId) {
        return commentRepository.findByPost_Id(postId);
    }

    @Override
    public List<Comment> getCommentsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return commentRepository.findByCreatedAtBetween(startDate, endDate);
    }

    @Override
    public List<Comment> getLatestComments(int count) {
        return commentRepository.findTopNByOrderByCreatedAtDesc(count);
    }

    public Comment updateComment(Long id, Optional<MultipartFile> file, Optional<String> content,
            Optional<Long> authorId) {
        Comment comment = getCommentById(id);
        if (comment != null) {
            if (file.isPresent()) {
                String filename = StringUtils.cleanPath(file.get().getOriginalFilename());
                Attachment attachment = new Attachment();
                attachment.setName(filename);
                attachment.setComment(comment);
                attachment.setAttachmentType(file.get().getContentType());
                attachment.setCreatedAt(LocalDateTime.now());

                String uploadDir = "src/main/resources/static/attachments";
                String attachmentLink = uploadDir + filename;
                FileUploadUtil.saveFile(uploadDir, filename, file.get());

                attachment.setAttachmentLink(attachmentLink);
                attachmentRepository.save(attachment);

                comment.setAttachment(attachment);
            }
            if (content.isPresent()) {
                comment.setContent(content.get());
            }
            if (authorId.isPresent()) {
                comment.setAuthor(userRepository.findById(authorId.get()).get());
            }

            if (ProfanityFilter.isProfanity(comment.getContent())) {
                comment.setFlagged(true);
            } else {
                comment.setFlagged(false);
            }

            comment = commentRepository.save(comment);
            return comment;
        } else {
            return null;
        }
    }

    public List<Post> getMostCommentedPosts() {
        List<Post> posts = postRepository.findAll();
        Collections.sort(posts, Comparator.comparingInt(post -> post.getComments().size()));
        Collections.reverse(posts);
        return posts;
    }

    public List<Comment> getCommentsWithMostReactions() {
        List<Comment> comments = commentRepository.findAll();
        Collections.sort(comments, Comparator.comparingInt(comment -> comment.getReactions().size()));
        Collections.reverse(comments);
        return comments;
    }

    public List<Reply> findRepliesByCommentId(Long commentId) {
        return commentRepository.findRepliesByCommentId(commentId);
    }

    public List<Comment> getMostRepliedComments() {
        return commentRepository.findAllOrderByRepliesDesc();
    }

    public Comment getParentCommentForReply(Long replyId) {
        Reply reply = replyRepository.findById(replyId).orElse(null);
        return reply.getComment();
    }
}
