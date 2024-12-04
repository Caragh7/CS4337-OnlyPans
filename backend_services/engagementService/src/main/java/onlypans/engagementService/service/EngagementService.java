package onlypans.engagementService.service;

import onlypans.common.entity.User;
import onlypans.engagementService.clients.UserServiceClient;
import onlypans.engagementService.entity.Comments;
import onlypans.engagementService.entity.Likes;
import onlypans.engagementService.repository.CommentRepository;
import onlypans.engagementService.repository.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EngagementService {

    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;
    private final UserServiceClient userServiceClient;

    @Autowired
    public EngagementService(
            LikeRepository likeRepository,
            CommentRepository commentRepository,
            UserServiceClient userServiceClient
    ) {
        this.likeRepository = likeRepository;
        this.commentRepository = commentRepository;
        this.userServiceClient = userServiceClient;
    }
    public Likes addLike(Long postId, Long userId) {
        if (postId == null || userId == null) {
            throw new IllegalArgumentException("postId and userId must not be null");
        }

        boolean alreadyLiked = likeRepository.existsByPostIdAndUserId(postId, userId);
        if (alreadyLiked) {
            throw new IllegalStateException("User has already liked this post");
        }

        Likes like = new Likes();
        like.setPostId(postId);
        like.setUserId(userId);
        return likeRepository.save(like);
    }

    public void removeLike(Long postId, Long userId) {
        if (postId == null || userId == null) {
            throw new IllegalArgumentException("postId and userId cannot be null");
        }
        likeRepository.deleteByPostIdAndUserId(postId, userId);
    }


    public int getLikeCount(Long postId) {
        return likeRepository.countByPostId(postId);
    }

    public Comments addComment(Long postId, String userId, String text) {
        if (postId == null || userId == null) {
            throw new IllegalArgumentException("postId and userId must not be null");
        }
        Comments comment = new Comments();
        comment.setPostId(postId);
        comment.setUserId(userId);
        comment.setText(text);
        return commentRepository.save(comment);
    }

    public List<Comments> getComments(Long postId) {
        return commentRepository.findByPostId(postId);
    }

    public List<Comments> getCommentsWithUserNames(Long postId) {
        List<Comments> comments = commentRepository.findByPostId(postId);

        comments.forEach(comment -> {
            try {
                User user = userServiceClient.getUserById(comment.getUserId());
                comment.setAuthorName(user.getFirstName());
            } catch (Exception e) {
                comment.setAuthorName("Unknown User");
            }
        });

        return comments;
    }
}