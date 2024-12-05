package onlypans.postService.controller;

import onlypans.postService.entity.Post;
import onlypans.postService.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {
    // api endpoints for post application
    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody Post post, Authentication authentication) {
        String userId = authentication.getName();
        System.out.println("user id" + userId);
        return new ResponseEntity<>(postService.createPost(post, post.getMediaUrl(), userId), HttpStatus.CREATED);

    }


    @GetMapping("/{id:\\d+}")
    public ResponseEntity<Post> getPostById(@PathVariable Long id) {
        return postService.getPostById(id)
                .map(post -> new ResponseEntity<>(post, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/media_url")
    public ResponseEntity<String> getPresignedUrl(@RequestParam String fileName) {
        String presignedUrl = postService.getPresignedUrl(fileName);
        return new ResponseEntity<>(presignedUrl, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts() {
        List<Post> posts = postService.getAllPosts();
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping("/your-feed")
    public List<Post> getYourFeed(Authentication authentication) {
        String userId = authentication.getName();
        return postService.getPostsForSubscribedCreators(userId);
    }

}