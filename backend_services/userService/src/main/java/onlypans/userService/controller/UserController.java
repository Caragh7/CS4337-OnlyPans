package onlypans.userService.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import onlypans.common.dtos.CreatorProfileRequest;
import onlypans.common.entity.User;
import onlypans.userService.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<User> createUser(@RequestBody @Valid User user) {
        return new ResponseEntity<>(userService.createUser(user), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public Optional<User> getUserById(@PathVariable String id) {
        return userService.getUserById(id);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody User userDetails) {
        return new ResponseEntity<>(userService.updateUser(id, userDetails), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/upgrade")
    public ResponseEntity<String> upgradeToCreatorProfile(
            @RequestBody CreatorProfileRequest request,
            Authentication authentication) {

        String userId = authentication.getName();
        Optional<User> optionalUser = userService.getUserById(userId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();


            request.setUserId(userId);
            request.setFirstName(user.getFirstName());
            request.setLastName(user.getLastName());
            userService.upgradeToCreatorProfile(request);
            return ResponseEntity.ok("User upgraded to creator profile");
        } else {
            // Handle the case where the user is not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found");
        }
    }
}


