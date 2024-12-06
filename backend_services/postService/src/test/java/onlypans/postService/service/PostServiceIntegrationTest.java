//package onlypans.postService.service;
//
//import onlypans.postService.entity.Post;
//import onlypans.postService.repository.PostRepository;
//import onlypans.postService.security.TestSecurityConfig;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestInstance;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//
//import org.springframework.context.annotation.Import;
//import org.springframework.security.oauth2.jwt.JwtDecoder;
//import org.springframework.test.context.ActiveProfiles;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//
//@ActiveProfiles("test")
//@Import(TestSecurityConfig.class)
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@SpringBootTest(properties = {
//        "spring.datasource.url=jdbc:h2:mem:test",
//        "spring.datasource.driver-class-name=org.h2.Driver",
//        "spring.jpa.hibernate.ddl-auto=create-drop",
//        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
//        "media.service.url=http://localhost:8082",
//        "spring.cloud.consul.enabled=false",
//        "spring.cloud.consul.discovery.enabled=false",
//        "spring.cloud.consul.config.enabled=false"
//})
//class PostServiceIntegrationTest {
//
//    @Autowired
//    private PostService postService;
//
//    @Autowired
//    private PostRepository postRepository;
//
//    @MockBean
//    private JwtDecoder jwtDecoder;
//
//    @BeforeAll
//    void setupDatabase() {
//        Post post = new Post();
//        post.setContentDescription("Test Post");
//        post.setMediaUrl("image.png");
//        postRepository.save(post);
//    }
//
//    @Test
//    void testCreatePost() {
//        Post post = new Post();
//        post.setContentDescription("New Post");
//        post.setMediaUrl("new_image.png");
//
//        Post result = postService.createPost(post, post.getMediaUrl());
//
//        assertNotNull(result.getId());
//        assertEquals("New Post", result.getContentDescription());
//    }
//
//    @Test
//    void testGetPostById() {
//        Post result = postService.getPostById(1L).orElse(null);
//
//        assertNotNull(result);
//        assertEquals("Test Post", result.getContentDescription());
//    }
//}
