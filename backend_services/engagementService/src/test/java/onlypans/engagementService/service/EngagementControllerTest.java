package onlypans.engagementService.service;

import onlypans.engagementService.controller.EngagementController;
import onlypans.engagementService.entity.Comments;
import onlypans.engagementService.entity.Likes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import java.util.Collections;
import java.util.List;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EngagementController.class)
class EngagementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EngagementService engagementService;

    @Test
    void testLikePost() throws Exception {
        Likes like = new Likes();
        when(engagementService.addLike(1L, "2L")).thenReturn(like);

        mockMvc.perform(post("/engagements/likes")
                        .param("postId", "1")
                        .param("userId", "2"))
                .andExpect(status().isCreated());
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
        when(engagementService.addComment(1L, "2L", "Nice post!")).thenReturn(comment);

        mockMvc.perform(post("/engagements/comments")
                        .param("postId", "1")
                        .param("userId", "2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("\"test comment...\""))
                .andExpect(status().isCreated());
    }

    @Test
    void testDislikePost() throws Exception {
        doNothing().when(engagementService).removeLike(1L, "2L");

        mockMvc.perform(delete("/engagements/likes")
                        .param("postId", "1")
                        .param("userId", "2"))
                .andExpect(status().isNoContent());

        verify(engagementService, times(1)).removeLike(1L, "2L");

    }

    @Test
    void testGetCommentsByPost() throws Exception {

        List<Comments> comments = Collections.singletonList(new Comments());
        when(engagementService.getComments(1L)).thenReturn(comments);

        mockMvc.perform(get("/engagements/comments/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(engagementService, times(1)).getComments(1L);
    }
}
