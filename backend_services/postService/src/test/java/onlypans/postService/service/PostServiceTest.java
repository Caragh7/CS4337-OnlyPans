//package onlypans.postService.service;
//
//import onlypans.postService.entity.Post;
//import onlypans.postService.repository.PostRepository;
//import onlypans.postService.security.TestSecurityConfig;
//import org.junit.jupiter.api.*;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.Import;
//import org.springframework.security.oauth2.jwt.JwtDecoder;
//import org.springframework.test.util.ReflectionTestUtils;
//
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@SpringBootTest(properties = {
//        "spring.cloud.consul.enabled=false",
//        "spring.cloud.consul.discovery.enabled=false",
//        "media.service.url=http://localhost:8080"
//})
//@Import(TestSecurityConfig.class)
//class PostServiceTest {
//
//    @Mock
//    private PostRepository postRepository;
//
//    @InjectMocks
//    private PostService postService;
//
//    @MockBean
//    private JwtDecoder jwtDecoder;
//
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        ReflectionTestUtils.setField(postService, "mediaServiceUrl", "http://localhost:8080");
//    }
//    @Test
//    void testCreatePost() {
//        Post post = new Post();
//        post.setContentDescription("Test Post");
//        post.setMediaUrl("image.png");
//
//        when(postRepository.save(any(Post.class))).thenAnswer(invocation -> invocation.getArgument(0));
//
//        Post result = postService.createPost(post, post.getMediaUrl());
//
//        assertNotNull(result);
//        assertEquals("Test Post", result.getContentDescription());
//        assertEquals("https://cdn-api.kobos.io/onlypans-content/image.png", result.getMediaUrl());
//        verify(postRepository, times(1)).save(post);
//    }
//
//    @Test
//    void testGetPostById() {
//        Post post = new Post();
//        post.setId(1L);
//        post.setContentDescription("Test Post");
//
//        when(postRepository.findById(1L)).thenReturn(Optional.of(post));
//
//        Optional<Post> result = postService.getPostById(1L);
//
//        assertTrue(result.isPresent());
//        assertEquals("Test Post", result.get().getContentDescription());
//        verify(postRepository, times(1)).findById(1L);
//    }
//
//    @Test
//    void testGetPostByIdNotFound() {
//        when(postRepository.findById(1L)).thenReturn(Optional.empty());
//
//        Optional<Post> result = postService.getPostById(1L);
//
//        assertFalse(result.isPresent());
//        verify(postRepository, times(1)).findById(1L);
//    }
//
//    @Test
//    void testGetAllPosts() {
//        postService.getAllPosts();
//        verify(postRepository, times(1)).findAll();
//    }
//
//    @Test
//    void testDeletePost() {
//        postService.deletePost(1L);
//        verify(postRepository, times(1)).deleteById(1L);
//    }
//
//
//}
