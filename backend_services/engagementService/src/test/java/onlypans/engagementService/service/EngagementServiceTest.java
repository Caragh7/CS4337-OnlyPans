package onlypans.engagementService.service;

import onlypans.engagementService.entity.Comments;
import onlypans.engagementService.entity.Likes;
import onlypans.engagementService.repository.CommentRepository;
import onlypans.engagementService.repository.LikeRepository;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EngagementServiceTest {

    @Mock
    private LikeRepository likeRepository;

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private EngagementService engagementService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddLike() {
        Long postId = 1L;
        Long userId = 2L;

        when(likeRepository.existsByPostIdAndUserId(postId, userId)).thenReturn(false);

        Likes like = new Likes();
        like.setPostId(postId);
        like.setUserId(userId);

        when(likeRepository.save(any(Likes.class))).thenReturn(like);

        Likes result = engagementService.addLike(postId, userId);

        assertNotNull(result);
        assertEquals(postId, result.getPostId());
        assertEquals(userId, result.getUserId());

        verify(likeRepository, times(1)).existsByPostIdAndUserId(postId, userId);
        verify(likeRepository, times(1)).save(any(Likes.class));
    }

    @Test
    void testAddLikeAlreadyLiked() {
        Long postId = 1L;
        Long userId = 2L;

        when(likeRepository.existsByPostIdAndUserId(postId, userId)).thenReturn(true);

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            engagementService.addLike(postId, userId);
        });

        assertEquals("User has already liked this post", exception.getMessage());

        verify(likeRepository, never()).save(any(Likes.class));
    }

    @Test
    void testAddLikeWithNullValues() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            engagementService.addLike(null, 2L);
        });
        assertEquals("postId and userId must not be null", exception.getMessage());

        exception = assertThrows(IllegalArgumentException.class, () -> {
            engagementService.addLike(1L, null);
        });
        assertEquals("postId and userId must not be null", exception.getMessage());
    }

    @Test
    void testRemoveLike() {
        Long postId = 1L;
        Long userId = 2L;

        doNothing().when(likeRepository).deleteByPostIdAndUserId(postId, userId);

        engagementService.removeLike(postId, userId);

        verify(likeRepository, times(1)).deleteByPostIdAndUserId(postId, userId);
    }

    @Test
    void testRemoveLikeWithNullValues() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            engagementService.removeLike(null, 2L);
        });
        assertEquals("postId and userId cannot be null", exception.getMessage());

        exception = assertThrows(IllegalArgumentException.class, () -> {
            engagementService.removeLike(1L, null);
        });
        assertEquals("postId and userId cannot be null", exception.getMessage());
    }


    @Test
    void testAddComment() {
        Long postId = 1L;
        String userId = 2L;
        String text = "test comment...";

        Comments comment = new Comments();
        comment.setPostId(postId);
        comment.setUserId(userId);
        comment.setText(text);

        when(commentRepository.save(any(Comments.class))).thenReturn(comment);

        Comments result = engagementService.addComment(postId, userId, text);

        assertNotNull(result);
        assertEquals(postId, result.getPostId());
        assertEquals(userId, result.getUserId());
        assertEquals(text, result.getText());

        verify(commentRepository, times(1)).save(any(Comments.class));
    }

    @Test
    void testGetComments() {
        Long postId = 1L;

        Comments comment1 = new Comments();
        comment1.setPostId(postId);
        comment1.setUserId(2L);
        comment1.setText("comment 1");

        Comments comment2 = new Comments();
        comment2.setPostId(postId);
        comment2.setUserId(3L);
        comment2.setText("comment 2");

        List<Comments> mockComments = Arrays.asList(comment1, comment2);

        when(commentRepository.findByPostId(postId)).thenReturn(mockComments);

        List<Comments> result = engagementService.getComments(postId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("comment 1", result.get(0).getText());
        assertEquals("comment 2", result.get(1).getText());

        verify(commentRepository, times(1)).findByPostId(postId);
    }


}
