package onlypans.postService.service;

import onlypans.common.entity.CreatorProfile;
import onlypans.postService.clients.SubscriptionServiceClient;
import onlypans.postService.clients.CreatorServiceClient;
import onlypans.postService.entity.Post;
import onlypans.postService.repository.PostRepository;
import onlypans.postService.security.TestSecurityConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:test",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "media.service.url=http://localhost:8082",
        "spring.cloud.consul.enabled=false",
        "spring.cloud.consul.discovery.enabled=false",
        "spring.cloud.consul.config.enabled=false"
})
class PostServiceIntegrationTest {

    @Autowired
    private PostService postService;

    @MockBean
    private PostRepository postRepository;

    @MockBean
    private SubscriptionServiceClient subscriptionServiceClient;

    @MockBean
    private CreatorServiceClient creatorServiceClient;


    @MockBean
    private JwtDecoder jwtDecoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @BeforeEach
    void setupDatabase() {
        Post post = new Post();
        post.setId(1L);
        post.setContentDescription("Test Post");
        post.setMediaUrl("image.png");

        when(postRepository.save(any(Post.class))).thenAnswer(invocation -> {
            Post savedPost = invocation.getArgument(0);
            savedPost.setId(1L);
            return savedPost;
        });

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        when(postRepository.findAll()).thenReturn(List.of(post));
    }

    @Test
    void testCreatePost() {
        Post post = new Post();
        post.setContentDescription("New Post");
        post.setMediaUrl("new_image.png");

        // Mock CreatorServiceClient response
        when(creatorServiceClient.getCreatorProfileByUserId("testUser"))
                .thenReturn(new CreatorProfile("testUser", "testFirstName", "testLastName", "testSrtipe"));

        // Test the service method
        Post result = postService.createPost(post, post.getMediaUrl(), "testUser");

        // Assertions
        assertNotNull(result.getId());
        assertEquals("New Post", result.getContentDescription());
    }

    @Test
    void testGetPostById() {
        Post result = postService.getPostById(1L).orElse(null);

        assertNotNull(result);
        assertEquals("Test Post", result.getContentDescription());
    }

    @Test
    void testGetPostsForSubscribedCreators() {
        List<Long> creatorProfileIds = List.of(1L, 2L, 3L);

        Post post1 = new Post();
        post1.setId(1L);
        post1.setContentDescription("Test Post 1");
        post1.setMediaUrl("url1");
        post1.setAuthorName("Author 1");
        post1.setCreatorId(1L);

        Post post2 = new Post();
        post2.setId(2L);
        post2.setContentDescription("Test Post 2");
        post2.setMediaUrl("url2");
        post2.setAuthorName("Author 2");
        post2.setCreatorId(2L);

        List<Post> posts = List.of(post1, post2);

        when(subscriptionServiceClient.getCreatorProfileIdsForUser("testUser")).thenReturn(creatorProfileIds);
        when(postRepository.findPostsBySubscribedCreators(creatorProfileIds)).thenReturn(posts);

        List<Post> result = postService.getPostsForSubscribedCreators("testUser");

        assertEquals(2, result.size());
        assertEquals("Test Post 1", result.getFirst().getContentDescription());
    }


    @Test
    void testGetAllPosts() {
        List<Post> posts = postService.getAllPosts();

        assertFalse(posts.isEmpty());
        assertEquals(1, posts.size());
        assertEquals("Test Post", posts.getFirst().getContentDescription());
    }
}

