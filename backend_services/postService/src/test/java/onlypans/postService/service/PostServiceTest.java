package onlypans.postService.service;

import onlypans.common.entity.CreatorProfile;
import onlypans.postService.clients.CreatorServiceClient;
import onlypans.postService.clients.SubscriptionServiceClient;
import onlypans.postService.entity.Post;
import onlypans.postService.repository.PostRepository;
import onlypans.postService.security.TestSecurityConfig;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(properties = {
        "spring.cloud.consul.enabled=false",
        "spring.cloud.consul.discovery.enabled=false",
        "media.service.url=http://localhost:8080"

})
@Import(TestSecurityConfig.class)
class PostServiceTest {
    @MockBean
    private PostRepository postRepository;

    @MockBean
    private CreatorServiceClient creatorServiceClient;

    @MockBean
    private SubscriptionServiceClient subscriptionServiceClient;

    @Autowired
    private PostService postService;

    @MockBean
    private JwtDecoder jwtDecoder;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(postService, "mediaServiceUrl", "http://localhost:8080");
    }
    @Test
    void testCreatePost() {
        Post post = new Post();
        post.setContentDescription("Test Post");
        post.setMediaUrl("image.png");


        CreatorProfile creatorProfile = new CreatorProfile();
        creatorProfile.setId(1L);
        creatorProfile.setUserId("testUser");

        // Mocking creator service call
        when(creatorServiceClient.getCreatorProfileByUserId("testUser")).thenReturn(creatorProfile);
        when(postRepository.save(any(Post.class))).thenAnswer(invocation -> invocation.getArgument(0));


        when(postRepository.save(any(Post.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Post result = postService.createPost(post, post.getMediaUrl(), "testUser");

        assertNotNull(result);
        assertEquals("Test Post", result.getContentDescription());
        assertEquals("https://cdn-api.kobos.io/onlypans-content/image.png", result.getMediaUrl());
        assertEquals(1L, result.getCreatorId());
        verify(postRepository, times(1)).save(post);
        verify(creatorServiceClient, times(1)).getCreatorProfileByUserId("testUser");
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
        assertEquals("Test Post 1", result.get(0).getContentDescription());
        assertEquals("Test Post 2", result.get(1).getContentDescription());
        verify(subscriptionServiceClient, times(1)).getCreatorProfileIdsForUser("testUser");
        verify(postRepository, times(1)).findPostsBySubscribedCreators(creatorProfileIds);
    }

    @Test
    void testGetPostsForSubscribedCreatorsNoSubscriptions() {
        when(subscriptionServiceClient.getCreatorProfileIdsForUser("testUser")).thenReturn(Collections.emptyList());

        List<Post> result = postService.getPostsForSubscribedCreators("testUser");

        assertTrue(result.isEmpty());
        verify(subscriptionServiceClient, times(1)).getCreatorProfileIdsForUser("testUser");
        verify(postRepository, never()).findPostsBySubscribedCreators(any());
    }



    @Test
    void testGetPostById() {
        Post post = new Post();
        post.setId(1L);
        post.setContentDescription("Test Post");

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        Optional<Post> result = postService.getPostById(1L);

        assertTrue(result.isPresent());
        assertEquals("Test Post", result.get().getContentDescription());
        verify(postRepository, times(1)).findById(1L);
    }

    @Test
    void testGetPostByIdNotFound() {
        when(postRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Post> result = postService.getPostById(1L);

        assertFalse(result.isPresent());
        verify(postRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAllPosts() {
        // Arrange
        List<Long> posts = List.of(1L, 2L, 3L);
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

        List<Post> mockPosts = List.of(post1, post2);
        when(postRepository.findAll()).thenReturn(mockPosts);

        // Act
        List<Post> result = postService.getAllPosts();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(postRepository, times(1)).findAll();
    }

    @Test
    void testDeletePost() {
        postService.deletePost(1L);
        verify(postRepository, times(1)).deleteById(1L);
    }


}
