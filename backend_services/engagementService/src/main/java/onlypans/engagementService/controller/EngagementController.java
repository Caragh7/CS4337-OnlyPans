package onlypans.engagementService.controller;

import onlypans.engagementService.entity.Comments;
import onlypans.engagementService.entity.Likes;
import onlypans.engagementService.service.EngagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/engagements")
public class EngagementController {

    @Autowired
    private EngagementService engagementService;

    @PostMapping("/likes")
    public ResponseEntity<Likes> likePost(@RequestParam Long postId, @RequestParam Long userId) {
        return new ResponseEntity<>(engagementService.addLike(postId, userId), HttpStatus.CREATED);
    }

    @DeleteMapping("/likes")
    public ResponseEntity<Void> unlikePost(@RequestParam Long postId, @RequestParam Long userId) {
        engagementService.removeLike(postId, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/likes/{postId}")
    public ResponseEntity<Integer> getLikeCount(@PathVariable Long postId) {
        return new ResponseEntity<>(engagementService.getLikeCount(postId), HttpStatus.OK);
    }

    @PostMapping("/comments")
    public ResponseEntity<Comments> commentOnPost(
            @RequestParam Long postId,
            @RequestParam Long userId,
            @RequestBody String text) {
        return new ResponseEntity<>(engagementService.addComment(postId, userId, text), HttpStatus.CREATED);
    }

    @GetMapping("/comments/{postId}")
    public ResponseEntity<List<Comments>> getComments(@PathVariable Long postId) {
        return new ResponseEntity<>(engagementService.getComments(postId), HttpStatus.OK);
    }
}
