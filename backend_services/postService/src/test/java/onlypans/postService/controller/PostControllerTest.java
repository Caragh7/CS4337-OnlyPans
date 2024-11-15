package onlypans.postService.controller;

import onlypans.postService.entity.Post;
import onlypans.postService.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PostController.class)
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    @Test
    void testCreatePost() throws Exception {
        Post post = new Post();
        post.setId(1L);
        post.setContentDescription("Test Post");
        post.setMediaUrl("image.jpg");

        when(postService.createPost(any(Post.class), anyString())).thenReturn(post);

        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"contentDescription\": \"Test Post\", \"mediaUrl\": \"image.jpg\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.contentDescription").value("Test Post"))
                .andExpect(jsonPath("$.mediaUrl").value("image.jpg"));
    }

    @Test
    void testGetPostById() throws Exception {
        Post post = new Post();
        post.setId(1L);
        post.setContentDescription("Test Post");

        when(postService.getPostById(1L)).thenReturn(Optional.of(post));

        mockMvc.perform(get("/posts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contentDescription").value("Test Post"));
    }

    @Test
    void testGetPostByIdNotFound() throws Exception {
        when(postService.getPostById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/posts/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testGetAllPosts() throws Exception {
        when(postService.getAllPosts()).thenReturn(List.of(new Post(), new Post()));

        mockMvc.perform(get("/posts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }
}
