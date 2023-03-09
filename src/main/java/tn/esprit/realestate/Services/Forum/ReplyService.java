package tn.esprit.realestate.Services.Forum;

import jakarta.mail.MessagingException;

import jakarta.servlet.http.HttpServletRequest;
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
import tn.esprit.realestate.Config.JwtService;
import tn.esprit.realestate.Entities.Forum.Attachment;
import tn.esprit.realestate.Entities.Forum.Reply;
import tn.esprit.realestate.Entities.User;
import tn.esprit.realestate.IServices.Forum.IReplyService;
import tn.esprit.realestate.Repositories.Forum.AttachmentRepository;
import tn.esprit.realestate.Repositories.Forum.CommentRepository;
import tn.esprit.realestate.Repositories.Forum.ReplyRepository;
import tn.esprit.realestate.Repositories.UserRepository;
import tn.esprit.realestate.Utils.FileUploadUtil;
import tn.esprit.realestate.Utils.ProfanityFilter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReplyService implements IReplyService {

    @Autowired
    private ReplyRepository replyRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private AttachmentRepository attachmentRepository;
    @Autowired
    private Environment env;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private HttpServletRequest request;
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

    public ResponseEntity<String> createReply(Optional<MultipartFile> file, String content, Long commentId)
            throws MessagingException {
        Reply reply = new Reply();
        reply.setContent(content);
        reply.setComment(commentRepository.findById(commentId).get());

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        jwt = authHeader.substring(7);
        userEmail = jwtService.extractUsername(jwt);
        User author = userRepository.findByEmail(userEmail).orElse(null);
        if (author == null) {
            return ResponseEntity.badRequest().body("Author not found");
        }
        if (ProfanityFilter.isProfanity(content)) {
            reply.setFlagged(true);
        }

        reply.setCreatedAt(LocalDateTime.now());
        reply.setAuthor(author);

        if (file.isPresent()) {
            String filename = StringUtils.cleanPath(file.get().getOriginalFilename());
            Attachment attachment = new Attachment();
            attachment.setName(filename);
            attachment.setReply(reply);
            attachment.setAttachmentType(file.get().getContentType());
            attachment.setCreatedAt(LocalDateTime.now());

            String uploadDir = "src/main/resources/static/attachments";
            String attachmentLink = uploadDir + filename;
            FileUploadUtil.saveFile(uploadDir, filename, file.get());

            attachment.setAttachmentLink(attachmentLink);
            attachmentRepository.save(attachment);

            reply.setAttachment(attachment);
        }

        if (reply.isFlagged()) {
            String to = "achrefpgm@gmail.com";
            String subject = "Reply flagged";
            String message = "Reply with id " + reply.getId() + " , created by User " + reply.getAuthor().getEmail()
                    + " has been flagged with Profanity content";
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(javaMailSender.createMimeMessage());
            mimeMessageHelper.setFrom(env.getProperty("spring.mail.username"));
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(message);
            javaMailSender.send(mimeMessageHelper.getMimeMessage());

            return ResponseEntity.badRequest().body("Profanity is not allowed");
        }

        replyRepository.save(reply);
        return ResponseEntity.ok("Reply created successfully");
    }

    public Reply updateReply(Long id, Optional<MultipartFile> file, Optional<String> content) {
        Reply reply = getReplyById(id);
        if (content.isPresent()) {
            reply.setContent(content.get());
        }
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        jwt = authHeader.substring(7);
        userEmail = jwtService.extractUsername(jwt);
        User author = userRepository.findByEmail(userEmail).orElse(null);
        if (author == null) {
            return null;
        }
        if (file.isPresent()) {
            String filename = StringUtils.cleanPath(file.get().getOriginalFilename());
            Attachment attachment = new Attachment();
            attachment.setName(filename);
            attachment.setReply(reply);
            attachment.setAttachmentType(file.get().getContentType());
            attachment.setCreatedAt(LocalDateTime.now());

            String uploadDir = "src/main/resources/static/attachments";
            String attachmentLink = uploadDir + filename;
            FileUploadUtil.saveFile(uploadDir, filename, file.get());

            attachment.setAttachmentLink(attachmentLink);
            attachmentRepository.save(attachment);

            reply.setAttachment(attachment);
        }
        replyRepository.save(reply);
        return reply;
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