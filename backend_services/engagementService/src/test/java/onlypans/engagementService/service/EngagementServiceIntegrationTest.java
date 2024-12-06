package onlypans.engagementService.service;

import jakarta.transaction.Transactional;
import onlypans.engagementService.entity.Comments;
import onlypans.engagementService.entity.Likes;
import onlypans.engagementService.repository.CommentRepository;
import onlypans.engagementService.repository.LikeRepository;
import onlypans.engagementService.security.TestSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import java.util.List;

import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;


// We do not need consul for our tests
@SpringBootTest(properties = {
        "spring.cloud.consul.config.enabled=false",
        "spring.cloud.consul.discovery.enabled=false",
        "spring.datasource.url=jdbc:h2:mem:test",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@ImportAutoConfiguration(exclude = {
        org.springframework.cloud.consul.serviceregistry.ConsulAutoServiceRegistrationAutoConfiguration.class,
        org.springframework.cloud.consul.serviceregistry.ConsulServiceRegistryAutoConfiguration.class
})
@Transactional
@Import(TestSecurityConfig.class)
class EngagementServiceIntegrationTest {

    @Autowired
    private EngagementService engagementService;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Test
    void testAddAndGetLike() {
        Likes like = engagementService.addLike(1L, "2L");
        assertNotNull(like);
        assertEquals(1L, like.getPostId());
        assertEquals("2L", like.getUserId());

        int count = engagementService.getLikeCount(1L);
        assertEquals(1, count);

        assertTrue(likeRepository.existsByPostIdAndUserId(1L, "2L"));
    }

    @Test
    void testAddAndGetComments() {
        Comments comment = engagementService.addComment(1L, "2L", "Test comment");
        assertNotNull(comment);
        assertEquals(1L, comment.getPostId());
        assertEquals("2L", comment.getUserId());
        assertEquals("Test comment", comment.getText());

        List<Comments> comments = engagementService.getComments(1L);
        assertNotNull(comments);
        assertEquals(1, comments.size());
        assertEquals("Test comment", comments.get(0).getText());
    }

    @Test
    void testAddDuplicateLike() {
        engagementService.addLike(1L, "2L");

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            engagementService.addLike(1L, "2L");
        });
        assertEquals("User has already liked this post", exception.getMessage());
    }

    @Test
    void testRemoveLike() {
        engagementService.addLike(1L, "2L");
        engagementService.removeLike(1L, "2L");

        assertFalse(likeRepository.existsByPostIdAndUserId(1L, "2L"));
    }
}