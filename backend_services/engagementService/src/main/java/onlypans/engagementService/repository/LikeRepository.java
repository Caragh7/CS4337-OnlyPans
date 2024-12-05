package onlypans.engagementService.repository;

import onlypans.engagementService.entity.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeRepository extends JpaRepository<Likes, Long> {
    List<Likes> findByPostId(Long postId);
    int countByPostId(Long postId);
    boolean existsByPostIdAndUserId(Long postId, String userId);
    void deleteByPostIdAndUserId(Long postId, String userId);
}


