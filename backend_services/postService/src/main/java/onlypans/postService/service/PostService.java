package onlypans.postService.service;

import onlypans.postService.entity.Post;
import onlypans.postService.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;


import java.util.List;
import java.util.Optional;

@Service
public class PostService {
// all methods the controller will use
    @Autowired
    private PostRepository postRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${media.service.url}")
    private String mediaServiceUrl;


    public Post createPost(Post post, String fileName) {
        String presignedUrl = restTemplate.getForObject(mediaServiceUrl + "/presigned-url?fileName=" + fileName, String.class);
        post.setMediaUrl(presignedUrl);
        return postRepository.save(post);
    }

    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    public String getPresignedUrl(String fileName) {
        return restTemplate.getForObject(mediaServiceUrl + "/presigned-url?fileName=" + fileName, String.class);
    }
}
