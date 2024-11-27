package onlypans.engagementService.service;

import onlypans.engagementService.entity.Comments;
import onlypans.engagementService.entity.Likes;
import onlypans.engagementService.repository.CommentRepository;
import onlypans.engagementService.repository.LikeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


// We do not need consul for our tests
@SpringBootTest(properties = {
        "spring.cloud.consul.config.enabled=false",
        "spring.cloud.consul.discovery.enabled=false"
})
@ImportAutoConfiguration(exclude = {
        org.springframework.cloud.consul.serviceregistry.ConsulAutoServiceRegistrationAutoConfiguration.class,
        org.springframework.cloud.consul.serviceregistry.ConsulServiceRegistryAutoConfiguration.class
})
class EngagementServiceIntegrationTest {


    @Autowired
    private EngagementService engagementService;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Test
    void testAddAndGetLike() {
        Likes like = engagementService.addLike(1L, 2L);

        assertNotNull(like);
        assertEquals(1L, like.getPostId());
        assertEquals(2L, like.getUserId());

        int count = engagementService.getLikeCount(1L);
        assertEquals(1, count);
    }

    @Test
    void testAddAndGetComments() {
        Comments comment = engagementService.addComment(1L, 2L, "Test comment");

        assertNotNull(comment);
        assertEquals(1L, comment.getPostId());
        assertEquals("Test comment", comment.getText());

        List<Comments> comments = engagementService.getComments(1L);
        assertEquals(1, comments.size());
        assertEquals("Test comment", comments.get(0).getText());
    }
}
