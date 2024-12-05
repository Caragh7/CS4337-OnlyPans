package onlypans.subscriptionService.repository;

import onlypans.subscriptionService.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findByUserId(String userId);

    @Query("SELECT s.creatorProfileId FROM Subscription s WHERE s.userId = :userId AND s.state = 'ACTIVE'")
    List<Long> findCreatorProfileIdsByUserId(@Param("userId") String userId);
    }
