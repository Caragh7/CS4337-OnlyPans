package onlypans.creatorService.controller;

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

    @PostMapping
    public ResponseEntity<CreatorProfile> createCreatorProfile(@RequestBody CreatorProfile creatorProfile) {
       CreatorProfile createdProfile = creatorProfileService.createCreatorProfile(creatorProfile);
           return new ResponseEntity<>(createdProfile, HttpStatus.CREATED);

    }

    @GetMapping("/{id}")
    public Optional<CreatorProfile> getCreatorProfileById(@PathVariable UUID id) {
        return creatorProfileService.getCreatorProfileById(id);
    }

    @GetMapping("/user/{userId}")
    public List<CreatorProfile> getCreatorProfilesByUserId(@PathVariable String userId) {
        return creatorProfileService.getCreatorProfilesByUserId(userId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CreatorProfile> updateCreatorProfile(@PathVariable UUID id, @RequestBody CreatorProfile creatorProfile) {
        return new ResponseEntity<>(creatorProfileService.updateCreatorProfile(id, creatorProfile), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCreatorProfile(@PathVariable UUID id) {
        creatorProfileService.deleteCreatorProfile(id);
        return ResponseEntity.noContent().build();
    }
}

