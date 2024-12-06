package onlypans.postService.controller;

import onlypans.postService.entity.Post;
import onlypans.postService.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PostControllerTest {

    @Mock
    private PostService postService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private PostController postController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreatePost() throws Exception {
        Post requestPost = new Post();
        requestPost.setContentDescription("Test Post");
        requestPost.setMediaUrl("url1");

        Post createdPost = new Post();
        createdPost.setId(1L);
        createdPost.setContentDescription("Test Post");
        createdPost.setMediaUrl("url1");

        // Mock JwtAuthenticationToken
        Jwt jwt = Jwt.withTokenValue("mock-token")
                .header("alg", "none")
                .claim("sub", "user123")
                .claim("given_name", "John")
                .claim("family_name", "Doe")
                .build();
        JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(jwt);

        // Mock behavior
        when(postService.createPost(any(Post.class), eq("url1"), eq("user123"))).thenReturn(createdPost);

        // Call controller method
        Post responsePost = postController.createPost(requestPost, authenticationToken).getBody();

        // Assertions
        assertEquals(createdPost.getId(), responsePost.getId());
        assertEquals("Test Post", responsePost.getContentDescription());
        verify(postService, times(1)).createPost(any(Post.class), eq("url1"), eq("user123"));
    }


    @Test
    void testGetAllPosts() {
        Post post = new Post();
        post.setId(1L);
        post.setContentDescription("Test Post");

        when(postService.getAllPosts()).thenReturn(Collections.singletonList(post));

        List<Post> posts = postController.getAllPosts().getBody();

        assertEquals(1, posts.size());
        assertEquals("Test Post", posts.get(0).getContentDescription());
        verify(postService, times(1)).getAllPosts();
    }

    @Test
    void testGetPostById() {
        Post post = new Post();
        post.setId(1L);
        post.setContentDescription("Test Post");

        when(postService.getPostById(1L)).thenReturn(Optional.of(post));

        Post responsePost = postController.getPostById(1L).getBody();

        assertEquals("Test Post", responsePost.getContentDescription());
        verify(postService, times(1)).getPostById(1L);
    }

    @Test
    void testGetPostByIdNotFound() {
        when(postService.getPostById(1L)).thenReturn(Optional.empty());

        assertEquals(404, postController.getPostById(1L).getStatusCodeValue());
        verify(postService, times(1)).getPostById(1L);
    }
}
