package onlypans.engagementService.controller;

import onlypans.common.dtos.CreateCommentRequest;
import onlypans.engagementService.entity.Comments;
import onlypans.engagementService.entity.Likes;
import onlypans.engagementService.service.EngagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/engagements")
public class EngagementController {

    @Autowired
    private EngagementService engagementService;

    @PostMapping("/likes")
    public ResponseEntity<Likes> likePost(@RequestParam Long postId, Authentication authentication) {
        String userId = authentication.getName();
        return new ResponseEntity<>(engagementService.addLike(postId, String.valueOf(userId)), HttpStatus.CREATED);
    }

    @DeleteMapping("/likes")
    public ResponseEntity<Void> unlikePost(@RequestParam Long postId, Authentication authentication) {
        String userId = authentication.getName();
        engagementService.removeLike(postId, String.valueOf(userId));
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/getLikes/{postId}")
    public ResponseEntity<Integer> getLikeCount(@PathVariable Long postId, Authentication authentication) {
        return new ResponseEntity<>(engagementService.getLikeCount(postId), HttpStatus.OK);
    }

    @PostMapping("/likes/toggle")
    public ResponseEntity<String> toggleLike(@RequestParam Long postId, Authentication authentication) {
        String userId = authentication.getName();
        Likes like = engagementService.toggleLike(postId, String.valueOf(userId));

        if (like == null) {
            return new ResponseEntity<>("Like removed", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Like added", HttpStatus.CREATED);
        }
    }

    @PostMapping("/comments")
    public ResponseEntity<Comments> commentOnPost(
            @RequestBody CreateCommentRequest commentRequest, Authentication authentication) {
        return new ResponseEntity<>(engagementService.addComment(
                commentRequest.getPostId(),
                authentication.getName(),
                commentRequest.getCommentBody()),
                HttpStatus.CREATED);
    }

    @GetMapping("/comments/{postId}")
    public ResponseEntity<List<Comments>> getComments(@PathVariable Long postId) {
        return new ResponseEntity<>(engagementService.getCommentsWithUserNames(postId), HttpStatus.OK);
    }
}
