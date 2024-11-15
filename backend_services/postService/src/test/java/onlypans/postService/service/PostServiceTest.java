package onlypans.postService.service;

import onlypans.postService.entity.Post;
import onlypans.postService.repository.PostRepository;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;


class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreatePost() {
        Post post = new Post();
        post.setContentDescription("Test Description");
        post.setMediaUrl("test.jpg");
        post.setAuthorName("testtesttest");
        post.setTimestamp(LocalDateTime.now());

        when(postRepository.save(any(Post.class))).thenReturn(post);

        Post result = postService.createPost(post, "test.jpg");
        assertNotNull(result);
        assertEquals("https://cdn-api.kobos.io/onlypans-content/test.jpg", result.getMediaUrl());
        assertEquals("Test Description", result.getContentDescription());
        assertEquals("testtesttest", result.getAuthorName());
        assertNotNull(result.getTimestamp());
        verify(postRepository, times(1)).save(any(Post.class));
    }


    @Test
    void testGetPostById() {
        Post post = new Post();
        post.setId(1L);
        post.setContentDescription("test Post");

        when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        Optional<Post> result = postService.getPostById(1L);
        assertTrue(result.isPresent());
        assertEquals("test Post", result.get().getContentDescription());

        verify(postRepository, times(1)).findById(1L);
    }

    @Test
    void testGetAllPosts() {
        when(postRepository.findAll()).thenReturn(List.of(new Post(), new Post()));

        List<Post> posts = postService.getAllPosts();
        assertNotNull(posts);
        assertEquals(2, posts.size());

        verify(postRepository, times(1)).findAll();
    }

    @Test
    void testDeletePost() {
        doNothing().when(postRepository).deleteById(1L);

        postService.deletePost(1L);

        verify(postRepository, times(1)).deleteById(1L);
    }
}