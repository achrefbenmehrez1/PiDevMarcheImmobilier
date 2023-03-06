package tn.esprit.realestate.Services.Forum;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.realestate.Config.JwtService;
import tn.esprit.realestate.Dto.Forum.PostDto;
import tn.esprit.realestate.Entities.Forum.Attachment;
import tn.esprit.realestate.Entities.Forum.Post;
import tn.esprit.realestate.Entities.Forum.Tag;
import tn.esprit.realestate.Entities.User;
import tn.esprit.realestate.IServices.Forum.IPostService;
import tn.esprit.realestate.Repositories.Forum.AttachmentRepository;
import tn.esprit.realestate.Repositories.Forum.PostRepository;
import tn.esprit.realestate.Repositories.Forum.TagRepository;
import tn.esprit.realestate.Repositories.UserRepository;
import tn.esprit.realestate.Utils.FileUploadUtil;
import tn.esprit.realestate.Utils.ProfanityFilter;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService implements IPostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private AttachmentRepository attachmentRepository;
    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private JwtService jwtService;
    /*@Autowired
    private PostElasticsearchRepository postElasticsearchRepository;*/

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public Post getPostById(Long id) {
        Optional<Post> post = postRepository.findById(id);
        if (post.isPresent()) {
            return post.get();
        } else {
            return null;
        }
    }

    @Override
    public ResponseEntity<String> createPost(Optional<MultipartFile> file, String title, String content, List<String> tagNames) throws IOException, MessagingException {
        Post post = new Post();
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        jwt = authHeader.substring(7);
        userEmail = jwtService.extractUsername(jwt);
        User author = userRepository.findByEmail(userEmail).orElse(null);
        if (author == null) {
            return ResponseEntity.badRequest().body("Author not found");
        }
        if (ProfanityFilter.isProfanity(title) || ProfanityFilter.isProfanity(content) || tagNames.stream().anyMatch(ProfanityFilter::isProfanity)) {
            post.setFlagged(true);
        }
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null) {
            ip = request.getRemoteAddr();
        }

        String dbLocation = "src/main/resources/static/GeoLite2-City.mmdb";

        File database = new File(dbLocation);
        /*DatabaseReader dbReader = new DatabaseReader.Builder(database)
                .build();

        InetAddress ipAddress = InetAddress.getByName(ip);
        CityResponse response = dbReader.city(ipAddress);

        String countryName = response.getCountry().getName();
        String cityName = response.getCity().getName();
        String postal = response.getPostal().getCode();
        String state = response.getLeastSpecificSubdivision().getName();

        System.out.println("Country: " + countryName);
        System.out.println("City: " + cityName);
        System.out.println("Postal: " + postal);
        System.out.println("State: " + state);*/

        post.setTitle(title);
        post.setContent(content);
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

        List<Tag> tags = new ArrayList<>();
        for (String tagName : tagNames) {
            Tag tag = tagRepository.findByName(tagName);
            if (tag == null) {
                tag = new Tag(tagName);
                tagRepository.save(tag);
            }
            tags.add(tag);
        }
        post.setTags(tags);

        postRepository.save(post);
        if (post.isFlagged()) {
            String to = "achrefpgm@gmail.com";
            String subject = "Post flagged";
            String message = "Post with id " + post.getId() + " , created by User " + post.getAuthor().getEmail() + " has been flagged with Profanity content";
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(javaMailSender.createMimeMessage());
            mimeMessageHelper.setFrom("achref.benmehrez@esprit.tn");
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(message);
            javaMailSender.send(mimeMessageHelper.getMimeMessage());
            return ResponseEntity.badRequest().body("Profanity is not allowed");
        }

        return ResponseEntity.ok("Post created successfully");
    }

    @Override
    public Post updatePost(Long id, Optional<MultipartFile> file, Optional<String> title, Optional<String> content, Optional<List<String>> tagNames) throws IOException {
        Post post = postRepository.findById(id).orElse(null);
        if (post == null) {
            return null;
        }
        title.ifPresent(post::setTitle);
        content.ifPresent(post::setContent);
        if (tagNames.isPresent()) {
            List<Tag> tags = new ArrayList<>();
            for (String tagName : tagNames.get()) {
                Tag tag = tagRepository.findByName(tagName);
                if (tag == null) {
                    tag = new Tag(tagName);
                    tagRepository.save(tag);
                }
                tags.add(tag);
            }
            post.setTags(tags);
        }

        final String authHeader = request.getHeader("Authorization");
        if(authHeader != null && authHeader.startsWith("Bearer ")) {
            final String jwt;
            final String userEmail;
            jwt = authHeader.substring(7);
            userEmail = jwtService.extractUsername(jwt);
            User author = userRepository.findByEmail(userEmail).orElse(null);
            if(Objects.equals(author.getId(), post.getAuthor().getId()))
                post.setAuthor(author);
        } else {
            return null;
        }

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

        if(ProfanityFilter.isProfanity(post.getTitle()) || ProfanityFilter.isProfanity(post.getContent()) || post.getTags().stream().anyMatch(tag -> ProfanityFilter.isProfanity(tag.getName()))){
            post.setFlagged(true);
        }
        else{
            post.setFlagged(false);
        }

        postRepository.save(post);
        return post;
    }

    @Override
    public List<Post> getPostsByAuthor(Long authorId) {
        return postRepository.findByAuthor_Id(authorId);
    }

    @Override
    public List<Post> searchPostsByTitle(String title) {
        return postRepository.findByTitleContaining(title);
    }

    @Override
    public List<Post> getPostsByTag(String tag) {
        return postRepository.findByTags_Name(tag);
    }

    @Override
    public List<Post> getPostsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return postRepository.findByCreatedAtBetween(startDate, endDate);
    }

    @Override
    public List<Post> getLatestPosts(int count) {
        return postRepository.findTopNByOrderByCreatedAtDesc(count);
    }

    @Override
    public List<Post> getMostPopularPosts(int count) {
        return postRepository.findTopNByOrderByViewsDesc(count);
    }

    @Override
    public void deletePost(Long id) {
        Post post = getPostById(id);
        postRepository.delete(post);
    }

    @Override
    public void incrementViewsCount(Long postId) {
        Post post = postRepository.findById(postId).orElse(null);
        post.setViews(post.getViews() + 1);
        postRepository.save(post);
    }

    @Override
    public List<Post> getRelatedPosts(Long postId) {
        Post post = postRepository.findById(postId).orElse(null);
        List<Tag> tags = post.getTags();
        List<Post> relatedPosts = new ArrayList<>();
        for (Tag tag : tags) {
            List<Post> postsWithTag = postRepository.findByTagsContaining(tag);
            for (Post p : postsWithTag) {
                if (!p.equals(post) && !relatedPosts.contains(p)) {
                    relatedPosts.add(p);
                }
            }
        }
        return relatedPosts;
    }

    @Override
    public void approvePost(Long postId) throws MessagingException {
        Optional<Post> postOptional = postRepository.findById(postId);
        postOptional.ifPresent(post -> {
            post.setApproved(true);
            postRepository.save(post);
        });

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("achref.benmehrez@esprit.tn");
        helper.setTo(postRepository.findById(postId).get().getAuthor().getEmail());
        helper.setSubject("Your Post has been approved");
        helper.setText("Your Post on Vendor has been approved, thanks for being patient", true);
        javaMailSender.send(message);
    }

    @Override
    public List<PostDto> getUnapprovedPosts() {
        List<Post> unapprovedPosts = postRepository.findByApprovedFalse();
        return unapprovedPosts.stream()
                .map(post -> new PostDto(post.getId(), post.getTitle(), post.getContent(), post.getCreatedAt(),
                        post.getAuthor().getId(), post.getAuthor().getUsername(), post.getTags()))
                .collect(Collectors.toList());
    }

    /*public List<Post> search(String searchTerm) {
        SearchHits<Post> searchHits = postElasticsearchRepository.search(
                new NativeSearchQueryBuilder()
                        .withQuery(QueryBuilders.matchQuery("title", searchTerm))
                        .build()
        );
        return searchHits.get().map(SearchHit::getContent).collect(Collectors.toList());
    }*/
}