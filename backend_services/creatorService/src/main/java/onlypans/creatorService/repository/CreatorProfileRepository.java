package onlypans.creatorService.repository;

import onlypans.creatorService.entity.CreatorProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CreatorProfileRepository extends JpaRepository<CreatorProfile, UUID> {
    List<CreatorProfile> findByUserId(String userId);
}
