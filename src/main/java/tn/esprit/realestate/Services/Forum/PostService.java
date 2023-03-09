package tn.esprit.realestate.Services.Forum;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.detectlanguage.DetectLanguage;
import com.detectlanguage.errors.APIError;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;

import tn.esprit.realestate.Config.JwtService;
import tn.esprit.realestate.Config.TranslatorText;
import tn.esprit.realestate.Dto.Forum.PostDto;
import tn.esprit.realestate.Entities.Forum.Attachment;
import tn.esprit.realestate.Entities.Forum.Post;
import tn.esprit.realestate.Entities.Forum.Tag;
import tn.esprit.realestate.Entities.Forum.Translation;
import tn.esprit.realestate.Entities.Forum.TranslationResponse;
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
import java.net.InetAddress;
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
    @Autowired
    private Environment env;

    /*
     * @Autowired
     * private PostElasticsearchRepository postElasticsearchRepository;
     */

    @Override
    public List<Post> getAllPosts(String translateTo) {
        List<Post> posts = postRepository.findAll();
        if (translateTo != null) {
            posts = posts.stream().map(post -> {
                TranslatorText translateRequest = new TranslatorText();
                try {
                    String res = translateRequest.Post(post.getContent(), translateTo);
                    String trimmedJsonString = res.substring(1, res.length() - 1);

                    // create ObjectMapper instance
                    ObjectMapper objectMapper = new ObjectMapper();

                    // read customer.json file into a tree model
                    JsonNode rootNode = objectMapper.readTree(trimmedJsonString);
                    post.setContent(rootNode.path("translations").get(0).path("text").asText());

                    return post;
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    return null;
                }
            }).filter(Objects::nonNull).collect(Collectors.toList());
        }

        return posts;
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
    public ResponseEntity<String> createPost(Optional<MultipartFile> file, String title, String content,
            List<String> tagNames) throws IOException, MessagingException, GeoIp2Exception, APIError {
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
        if (ProfanityFilter.isProfanity(title) || ProfanityFilter.isProfanity(content)
                || tagNames.stream().anyMatch(ProfanityFilter::isProfanity)) {
            post.setFlagged(true);
        }

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://api.ipstack.com/check?access_key=c0b9e10374bb43a4ad75dc161326cee2")
                .get()
                .build();

        Response response = client.newCall(request).execute();
        String res = response.body().string();
        // create ObjectMapper instance
        ObjectMapper objectMapper = new ObjectMapper();

        // read customer.json file into a tree model
        JsonNode rootNode = objectMapper.readTree(res);
        post.setLongitude(rootNode.path("longitude").asDouble());
        post.setLatitude(rootNode.path("latitude").asDouble());
        post.setCountry(rootNode.path("country").asText());
        post.setCity(rootNode.path("city").asText());
        post.setZip(rootNode.path("zip").asInt());
        post.setCountry(rootNode.path("country").asText());

        DetectLanguage.apiKey = "10b6d005b806a6496c79c340c1c2d4c7";
        String language = DetectLanguage.simpleDetect(content);

        post.setLanguage(language);

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
            String message = "Post with id " + post.getId() + " , created by User " + post.getAuthor().getEmail()
                    + " has been flagged with Profanity content";
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(javaMailSender.createMimeMessage());
            mimeMessageHelper.setFrom(env.getProperty("spring.mail.username"));
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(message);
            javaMailSender.send(mimeMessageHelper.getMimeMessage());

            return ResponseEntity.badRequest().body("Profanity is not allowed");
        }

        return ResponseEntity.ok("Post created successfully");
    }

    @Override
    public Post updatePost(Long id, Optional<MultipartFile> file, Optional<String> title, Optional<String> content,
            Optional<List<String>> tagNames) throws IOException {
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
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            final String jwt;
            final String userEmail;
            jwt = authHeader.substring(7);
            userEmail = jwtService.extractUsername(jwt);
            User author = userRepository.findByEmail(userEmail).orElse(null);
            if (Objects.equals(author.getId(), post.getAuthor().getId()))
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

        if (ProfanityFilter.isProfanity(post.getTitle()) || ProfanityFilter.isProfanity(post.getContent())
                || post.getTags().stream().anyMatch(tag -> ProfanityFilter.isProfanity(tag.getName()))) {
            post.setFlagged(true);
        } else {
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

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(postRepository.findById(postId).get().getAuthor().getEmail());
        message.setSubject("Your Post has been approved");
        message.setText("Your Post on Vendor has been approved, thanks for being patient");
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

    /*
     * public List<Post> search(String searchTerm) {
     * SearchHits<Post> searchHits = postElasticsearchRepository.search(
     * new NativeSearchQueryBuilder()
     * .withQuery(QueryBuilders.matchQuery("title", searchTerm))
     * .build()
     * );
     * return
     * searchHits.get().map(SearchHit::getContent).collect(Collectors.toList());
     * }
     */
}