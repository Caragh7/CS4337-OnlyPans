package onlypans.engagementService.service;

import onlypans.engagementService.entity.Comments;
import onlypans.engagementService.entity.Likes;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


// We do not need consul for our tests
@SpringBootTest(properties = {
        "spring.cloud.consul.config.enabled=false",
        "spring.cloud.consul.discovery.enabled=false"
}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ImportAutoConfiguration(exclude = {
        org.springframework.cloud.consul.serviceregistry.ConsulAutoServiceRegistrationAutoConfiguration.class,
        org.springframework.cloud.consul.serviceregistry.ConsulServiceRegistryAutoConfiguration.class
})
@AutoConfigureMockMvc
class EngagementControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EngagementService engagementService;

    @Test
    void testLikePost() throws Exception {
        Likes like = new Likes();
        when(engagementService.addLike(1L, 2L)).thenReturn(like);

        mockMvc.perform(post("/engagements/likes")
                        .param("postId", "1")
                        .param("userId", "2"))
                .andExpect(status().isCreated());
    }

    @Test
    void testUnlikePost() throws Exception {
        Mockito.doNothing().when(engagementService).removeLike(1L, 2L);

        mockMvc.perform(delete("/engagements/likes")
                        .param("postId", "1")
                        .param("userId", "2"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetLikeCount() throws Exception {
        when(engagementService.getLikeCount(1L)).thenReturn(5);

        mockMvc.perform(get("/engagements/likes/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));
    }

    @Test
    void testCommentOnPost() throws Exception {
        Comments comment = new Comments();
        comment.setText("Nice post!");
        when(engagementService.addComment(1L, 2L, "test!")).thenReturn(comment);

        mockMvc.perform(post("/engagements/comments")
                        .param("postId", "1")
                        .param("userId", "2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("\"test!\""))
                .andExpect(status().isCreated());
    }

    @Test
    void testGetComments() throws Exception {
        Comments comment1 = new Comments();
        comment1.setText("First comment");
        Comments comment2 = new Comments();
        comment2.setText("Second comment");

        when(engagementService.getComments(1L)).thenReturn(Arrays.asList(comment1, comment2));

        mockMvc.perform(get("/engagements/comments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].text").value("First comment"))
                .andExpect(jsonPath("$[1].text").value("Second comment"));
    }
}
