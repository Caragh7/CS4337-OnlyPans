package onlypans.engagementService.controller;

import onlypans.engagementService.entity.Comments;
import onlypans.engagementService.entity.Likes;
import onlypans.engagementService.service.EngagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(EngagementController.class)
class EngagementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EngagementService engagementService;

    @Test
    @WithMockUser(username = "user123")
    void testLikePost() throws Exception {
        Likes like = new Likes();
        like.setPostId(1L);
        like.setUserId("user123");

        when(engagementService.addLike(1L, "user123")).thenReturn(like);

        mockMvc.perform(post("/engagements/likes")
                        .param("postId", "1")
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.postId").value(1))
                .andExpect(jsonPath("$.userId").value("user123"));

        verify(engagementService, times(1)).addLike(1L, "user123");
    }

    @Test
    @WithMockUser(username = "user123")
    void testGetLikeCount() throws Exception {
        when(engagementService.getLikeCount(1L)).thenReturn(5);

        mockMvc.perform(get("/engagements/getLikes/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));

        verify(engagementService, times(1)).getLikeCount(1L);
    }

    @Test
    @WithMockUser(username = "user123")
    void testCommentOnPost() throws Exception {
        Comments comment = new Comments();
        comment.setPostId(1L);
        comment.setUserId("user123");
        comment.setText("Nice post!");

        when(engagementService.addComment(1L, "user123", "Nice post!")).thenReturn(comment);

        mockMvc.perform(post("/engagements/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"postId\":1,\"commentBody\":\"Nice post!\"}")
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.postId").value(1))
                .andExpect(jsonPath("$.userId").value("user123"))
                .andExpect(jsonPath("$.text").value("Nice post!"));

        verify(engagementService, times(1)).addComment(1L, "user123", "Nice post!");
    }

    @Test
    @WithMockUser(username = "user123")
    void testDislikePost() throws Exception {
        doNothing().when(engagementService).removeLike(1L, "user123");

        mockMvc.perform(delete("/engagements/likes")
                        .param("postId", "1")
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(engagementService, times(1)).removeLike(1L, "user123");
    }

    @Test
    @WithMockUser(username = "user123")
    void testGetCommentsByPost() throws Exception {
        Comments comment = new Comments();
        comment.setId(1L);
        comment.setPostId(1L);
        comment.setUserId("user123");
        comment.setText("Nice post!");

        List<Comments> comments = Collections.singletonList(comment);

        when(engagementService.getCommentsWithUserNames(1L)).thenReturn(comments);

        mockMvc.perform(get("/engagements/comments/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].postId").value(1))
                .andExpect(jsonPath("$[0].userId").value("user123"))
                .andExpect(jsonPath("$[0].text").value("Nice post!"));

        verify(engagementService, times(1)).getCommentsWithUserNames(1L);
    }
}
