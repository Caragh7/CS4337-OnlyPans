package onlypans.engagementService.service;

import onlypans.engagementService.entity.Comments;
import onlypans.engagementService.entity.Likes;
import onlypans.engagementService.repository.CommentRepository;
import onlypans.engagementService.repository.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EngagementService {

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private CommentRepository commentRepository;

    public Likes addLike(Long postId, Long userId) {
        if (postId == null || userId == null) {
            throw new IllegalArgumentException("postId and userId must not be null");
        }
        Likes like = new Likes();
        like.setPostId(postId);
        like.setUserId(userId);
        return likeRepository.save(like);
    }

    public void removeLike(Long postId, Long userId) {
        likeRepository.deleteAll(likeRepository.findByPostId(postId));
    }

    public int getLikeCount(Long postId) {
        return likeRepository.countByPostId(postId);
    }

    public Comments addComment(Long postId, Long userId, String text) {
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
}
