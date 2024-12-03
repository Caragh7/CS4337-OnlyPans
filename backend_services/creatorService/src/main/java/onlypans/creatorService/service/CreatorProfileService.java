package onlypans.creatorService.service;

import jakarta.transaction.Transactional;
import onlypans.common.dtos.CreatePriceRequest;
import onlypans.common.dtos.CreatorProfileRequest;
import onlypans.common.exceptions.*;
import onlypans.common.entity.CreatorProfile;
import onlypans.creatorService.clients.SubscriptionServiceClient;
import onlypans.creatorService.repository.CreatorProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@EntityScan(basePackages = { "onlypans.common.entity"})
public class CreatorProfileService {

    private final CreatorProfileRepository creatorProfileRepository;
    private final SubscriptionServiceClient subscriptionServiceClient;

    @Autowired
    public CreatorProfileService(CreatorProfileRepository creatorProfileRepository, SubscriptionServiceClient subscriptionServiceClient) {
        this.creatorProfileRepository = creatorProfileRepository;
        this.subscriptionServiceClient = subscriptionServiceClient;
    }

    public CreatorProfile createCreatorProfile(CreatorProfileRequest request) {
        try {
            CreatePriceRequest createPriceRequest = new CreatePriceRequest();
            createPriceRequest.setPrice(request.getPrice());
            String id = this.subscriptionServiceClient.createPrice(createPriceRequest);

            CreatorProfile profile = new CreatorProfile(request.getUserId(), request.getFirstName(), request.getLastName(), id);
            return creatorProfileRepository.save(profile);
        } catch (Exception e) {
            throw new UnableToCreateResourceException("Unable to create Creator Profile", e);
        }
    }
    public List<CreatorProfile> getAllCreators() {
        try {
            return creatorProfileRepository.findAll();
        } catch (Exception e) {
            throw new ResourceNotFoundException("Unable to find all Creator Profiles");
        }
    }
    public Optional<CreatorProfile> getCreatorProfileById(Long id) {
        try {
            return creatorProfileRepository.findById(id);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Creator Profile not found with ID " + id);
        }
    }

    public CreatorProfile getCreatorProfileByUserId(String userId) {
        try {
            return creatorProfileRepository.findFirstByUserId(userId);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Creator Profile not found with userID " + userId);
        }
    }

    public CreatorProfile updateCreatorProfile(Long id, CreatorProfile updatedProfile) {
        return creatorProfileRepository.findById(id).map(profile -> {
            profile.setFirstName(updatedProfile.getFirstName());
            profile.setLastName(updatedProfile.getLastName());
            return creatorProfileRepository.save(profile);
        }).orElseThrow(() -> new ResourceNotFoundException("Creator Profile not found with ID " + id));
    }

    @Transactional
    public void deleteCreatorProfile(String id) {
        try {
            creatorProfileRepository.deleteByUserId(id);
        } catch (Exception e) {
            throw new UnableToDeleteResourceException("Unable to delete Creator Profile with ID " + id, e);
        }
    }
}
