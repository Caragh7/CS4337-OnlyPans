package onlypans.creatorService.controller;

import onlypans.common.dtos.CreatorProfileRequest;
import onlypans.common.entity.CreatorProfile;
import onlypans.creatorService.service.CreatorProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/creator-profiles")
public class CreatorProfileController {

    private final CreatorProfileService creatorProfileService;

    @Autowired
    public CreatorProfileController(CreatorProfileService creatorProfileService) {
        this.creatorProfileService = creatorProfileService;
    }
    @GetMapping
    public List<CreatorProfile> getAllCreatorProfiles() {
        return  creatorProfileService.getAllCreators();
    }

    @PostMapping("/create")
    public ResponseEntity<CreatorProfile> createCreatorProfile(@RequestBody CreatorProfileRequest request) {
       CreatorProfile createdProfile = creatorProfileService.createCreatorProfile(request);
           return new ResponseEntity<>(createdProfile, HttpStatus.CREATED);

    }

    @GetMapping("/{id}")
    public Optional<CreatorProfile> getCreatorProfileById(@PathVariable Long id) {
        return creatorProfileService.getCreatorProfileById(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CreatorProfile> updateCreatorProfile(@PathVariable Long id, @RequestBody CreatorProfile creatorProfile) {
        return new ResponseEntity<>(creatorProfileService.updateCreatorProfile(id, creatorProfile), HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public CreatorProfile getCreatorProfileByUserId(@PathVariable String userId) {
        return creatorProfileService.getCreatorProfileByUserId(userId);
    }


    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteCreatorProfile(Authentication authentication) {
        creatorProfileService.deleteCreatorProfile(authentication.getName());
        return ResponseEntity.noContent().build();
    }
}

