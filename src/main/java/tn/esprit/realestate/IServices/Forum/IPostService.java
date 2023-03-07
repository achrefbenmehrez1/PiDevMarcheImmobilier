package tn.esprit.realestate.IServices.Forum;

import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.detectlanguage.errors.APIError;
import com.maxmind.geoip2.exception.GeoIp2Exception;

import tn.esprit.realestate.Dto.Forum.PostDto;
import tn.esprit.realestate.Entities.Forum.Post;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface IPostService {
        public List<Post> getAllPosts();

        public Post getPostById(Long id);

        public void deletePost(Long id);

        ResponseEntity<String> createPost(Optional<MultipartFile> file, String title, String content,
                        List<String> tagNames)
                        throws IOException, MessagingException, GeoIp2Exception, APIError;

        public Post updatePost(Long id, Optional<MultipartFile> file, Optional<String> title, Optional<String> content,
                        Optional<List<String>> tagNames) throws IOException;

        public List<Post> getPostsByAuthor(Long authorId);

        public List<Post> searchPostsByTitle(String title);

        public List<Post> getPostsByTag(String tag);

        public List<Post> getPostsByDateRange(LocalDateTime startDate, LocalDateTime endDate);

        public List<Post> getLatestPosts(int count);

        public List<Post> getMostPopularPosts(int count);

        public void incrementViewsCount(Long postId);

        public List<Post> getRelatedPosts(Long postId);

        void approvePost(Long postId) throws MessagingException;

        List<PostDto> getUnapprovedPosts();
}
