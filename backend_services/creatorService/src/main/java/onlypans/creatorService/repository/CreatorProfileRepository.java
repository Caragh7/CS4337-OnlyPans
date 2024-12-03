package onlypans.creatorService.repository;

import onlypans.common.entity.CreatorProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface CreatorProfileRepository extends JpaRepository<CreatorProfile, Long> {
    List<CreatorProfile> findByUserId(String userId);
    CreatorProfile findFirstByUserId(String userId);
    void deleteByUserId(String userId);
}
