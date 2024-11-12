package onlypans.creatorService.service;

import onlypans.common.dtos.CreatorProfileRequest;
import onlypans.common.exceptions.*;
import onlypans.creatorService.entity.CreatorProfile;
import onlypans.creatorService.repository.CreatorProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CreatorProfileService {

    private final CreatorProfileRepository creatorProfileRepository;

    @Autowired
    public CreatorProfileService(CreatorProfileRepository creatorProfileRepository) {
        this.creatorProfileRepository = creatorProfileRepository;
    }

    public CreatorProfile createCreatorProfile(CreatorProfileRequest request) {
        try {
            CreatorProfile profile = new CreatorProfile(request.getUserId(), request.getFirstName(), request.getLastName());
            return creatorProfileRepository.save(profile);
        } catch (Exception e) {
            throw new UnableToCreateResourceException("Unable to create Creator Profile", e);
        }
    }

    public Optional<CreatorProfile> getCreatorProfileById(Long id) {
        try {
            return creatorProfileRepository.findById(id);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Creator Profile not found with ID " + id);
        }
    }

    public List<CreatorProfile> getCreatorProfilesByUserId(Long userId) {
        try {
            return creatorProfileRepository.findByUserId(userId);
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

    public void deleteCreatorProfile(Long id) {
        try {
            creatorProfileRepository.deleteById(id);
        } catch (Exception e) {
            throw new UnableToDeleteResourceException("Unable to delete Creator Profile with ID " + id, e);
        }
    }
}
