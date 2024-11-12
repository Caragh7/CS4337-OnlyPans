package onlypans.creatorService.controller;

import onlypans.common.dtos.CreatorProfileRequest;
import onlypans.creatorService.entity.CreatorProfile;
import onlypans.creatorService.service.CreatorProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
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

    @PostMapping("/create")
    public ResponseEntity<CreatorProfile> createCreatorProfile(@RequestBody CreatorProfileRequest request) {
       CreatorProfile createdProfile = creatorProfileService.createCreatorProfile(request);
           return new ResponseEntity<>(createdProfile, HttpStatus.CREATED);

    }

    @GetMapping("/{id}")
    public Optional<CreatorProfile> getCreatorProfileById(@PathVariable Long id) {
        return creatorProfileService.getCreatorProfileById(id);
    }

    @GetMapping("/user/{userId}")
    public List<CreatorProfile> getCreatorProfilesByUserId(@PathVariable Long userId) {
        return creatorProfileService.getCreatorProfilesByUserId(userId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CreatorProfile> updateCreatorProfile(@PathVariable Long id, @RequestBody CreatorProfile creatorProfile) {
        return new ResponseEntity<>(creatorProfileService.updateCreatorProfile(id, creatorProfile), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCreatorProfile(@PathVariable Long id) {
        creatorProfileService.deleteCreatorProfile(id);
        return ResponseEntity.noContent().build();
    }
}

