package onlypans.postService.service;

import onlypans.common.entity.CreatorProfile;
import onlypans.postService.entity.Post;
import onlypans.postService.repository.PostRepository;
import onlypans.postService.clients.CreatorServiceClient;
import onlypans.postService.clients.SubscriptionServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.server.ResponseStatusException;


import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {
// all methods the controller will use
    private final PostRepository postRepository;
    private final CreatorServiceClient creatorServiceClient;
    private final SubscriptionServiceClient subscriptionServiceClient;

    @Autowired
    public PostService(PostRepository postRepository, CreatorServiceClient creatorServiceClient, SubscriptionServiceClient subscriptionServiceClient) {
        this.postRepository = postRepository;
        this.creatorServiceClient = creatorServiceClient;
        this.subscriptionServiceClient = subscriptionServiceClient;
    }

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${media.service.url}")
    private String mediaServiceUrl;


    public Post createPost(Post post, String mediaUrl, String userId) {

      CreatorProfile creatorProfile = creatorServiceClient.getCreatorProfileByUserId(userId);

        if (creatorProfile == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Creator profile not found for userId: " + userId);
        }



        String imageUrl = ("https://cdn-api.kobos.io/onlypans-content/" + mediaUrl);

        post.setMediaUrl(imageUrl);
        post.setCreatorId(creatorProfile.getId());
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

    public List<Post> getPostsForSubscribedCreators(String userId) {
        List<Long> creatorProfileIds = subscriptionServiceClient.getCreatorProfileIdsForUser(userId);
        if (creatorProfileIds.isEmpty()) {
            return Collections.emptyList();
        }


        return postRepository.findPostsBySubscribedCreators(creatorProfileIds);
    }

}
