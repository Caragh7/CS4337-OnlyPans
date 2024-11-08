package onlypans.creatorService.service;

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

    public CreatorProfile createCreatorProfile(CreatorProfile creatorProfile) {
        try {
            return creatorProfileRepository.save(creatorProfile);
        } catch (Exception e) {
            throw new UnableToCreateResourceException("Unable to create Creator Profile", e);
        }
    }

    public Optional<CreatorProfile> getCreatorProfileById(UUID id) {
        try {
            return creatorProfileRepository.findById(id);
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Creator Profile not found with ID " + id);
        }
    }

    public List<CreatorProfile> getCreatorProfilesByUserId(String userId) {
        try {
            return creatorProfileRepository.findByUserId(userId);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Creator Profile not found with userID " + userId);
        }
    }

    public CreatorProfile updateCreatorProfile(UUID id, CreatorProfile updatedProfile) {
        return creatorProfileRepository.findById(id).map(profile -> {
            profile.setFirstName(updatedProfile.getFirstName());
            profile.setLastName(updatedProfile.getLastName());
            return creatorProfileRepository.save(profile);
        }).orElseThrow(() -> new ResourceNotFoundException("Creator Profile not found with ID " + id));
    }

    public void deleteCreatorProfile(UUID id) {
        try {
            creatorProfileRepository.deleteById(id);
        } catch (Exception e) {
            throw new UnableToDeleteResourceException("Unable to delete Creator Profile with ID " + id, e);
        }
    }
}
