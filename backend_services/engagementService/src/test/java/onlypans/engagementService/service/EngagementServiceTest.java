package onlypans.engagementService.service;

import onlypans.engagementService.entity.Comments;
import onlypans.engagementService.entity.Likes;
import onlypans.engagementService.repository.CommentRepository;
import onlypans.engagementService.repository.LikeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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

    private Likes createMockLike(Long postId, String userId) {
        Likes like = new Likes();
        like.setPostId(postId);
        like.setUserId(userId);
        return like;
    }

    private Comments createMockComment(Long postId, String userId, String text) {
        Comments comment = new Comments();
        comment.setPostId(postId);
        comment.setUserId(userId);
        comment.setText(text);
        return comment;
    }

    @Test
    void testAddLike() {
        Long postId = 1L;
        String userId = "2L";

        when(likeRepository.existsByPostIdAndUserId(postId, userId)).thenReturn(false);
        Likes like = createMockLike(postId, userId);
        when(likeRepository.save(any(Likes.class))).thenReturn(like);

        Likes result = engagementService.addLike(postId, userId);

        assertNotNull(result);
        assertEquals(postId, result.getPostId());
        assertEquals(userId, result.getUserId());

        verify(likeRepository).existsByPostIdAndUserId(postId, userId);
        verify(likeRepository).save(any(Likes.class));
    }

    @Test
    void testGetLikeCount() {
        Long postId = 1L;
        when(likeRepository.countByPostId(postId)).thenReturn(5);

        int likeCount = engagementService.getLikeCount(postId);

        assertEquals(5, likeCount);
        verify(likeRepository).countByPostId(postId);
    }

    @Test
    void testAddCommentWithEmptyText() {
        Long postId = 1L;
        String userId = "2L";
        String text = " ";

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            engagementService.addComment(postId, userId, text);
        });

        assertEquals("comment text must not be empty", exception.getMessage());
        verify(commentRepository, never()).save(any(Comments.class));
    }

    @Test
    void testGetCommentsWithNoResults() {
        Long postId = 1L;
        when(commentRepository.findByPostId(postId)).thenReturn(Collections.emptyList());

        List<Comments> result = engagementService.getComments(postId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(commentRepository).findByPostId(postId);
    }

    @Test
    void testRepositoryExceptionHandling() {
        Long postId = 1L;
        String userId = "2L";

        doThrow(new RuntimeException("Database error"))
                .when(likeRepository)
                .existsByPostIdAndUserId(postId, userId);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            engagementService.addLike(postId, userId);
        });

        assertEquals("Database error", exception.getMessage());
        verify(likeRepository).existsByPostIdAndUserId(postId, userId);
    }

    @Test
    void testAddComment() {
        Long postId = 1L;
        String userId = "2L";
        String text = "Nice post!";

        Comments comment = createMockComment(postId, userId, text);
        when(commentRepository.save(any(Comments.class))).thenReturn(comment);

        Comments result = engagementService.addComment(postId, userId, text);

        assertNotNull(result);
        assertEquals(postId, result.getPostId());
        assertEquals(userId, result.getUserId());
        assertEquals(text, result.getText());

        verify(commentRepository).save(any(Comments.class));
    }

    @Test
    void testGetComments() {
        Long postId = 1L;

        Comments comment1 = createMockComment(postId, "2L", "Comment 1");
        Comments comment2 = createMockComment(postId, "3L", "Comment 2");

        when(commentRepository.findByPostId(postId)).thenReturn(Arrays.asList(comment1, comment2));

        List<Comments> result = engagementService.getComments(postId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Comment 1", result.get(0).getText());
        assertEquals("Comment 2", result.get(1).getText());

        verify(commentRepository).findByPostId(postId);
    }

    @Test
    void testToggleLike_NullPostId() {
        String userId = "user123";

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            engagementService.toggleLike(null, userId);
        });

        assertEquals("postId and userId can not be null", exception.getMessage());
        verify(likeRepository, never()).existsByPostIdAndUserId(any(), any());
    }

    @Test
    void testToggleLike_NullUserId() {
        Long postId = 1L;

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            engagementService.toggleLike(postId, null);
        });

        assertEquals("postId and userId can not be null", exception.getMessage());
        verify(likeRepository, never()).existsByPostIdAndUserId(any(), any());
    }
}
