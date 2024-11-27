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

    @Test
    void testAddLike() {
        Long postId = 1L;
        Long userId = 2L;

        Likes like = new Likes();
        like.setPostId(postId);
        like.setUserId(userId);

        when(likeRepository.save(any(Likes.class))).thenReturn(like);

        Likes result = engagementService.addLike(postId, userId);

        assertNotNull(result);
        assertEquals(postId, result.getPostId());
        assertEquals(userId, result.getUserId());

        verify(likeRepository, times(1)).save(any(Likes.class));
    }

    @Test
    void testAddComment() {
        Long postId = 1L;
        Long userId = 2L;
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

    // removeLike, getLikeCount, getComments, check for duplicates maybe?


}
