package onlypans.userService.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import onlypans.common.dtos.CreatorProfileRequest;
import onlypans.common.exceptions.*;
import onlypans.common.entity.User;
import onlypans.userService.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;


import java.util.List;
import java.util.Optional;

@Service
@EntityScan(basePackages = { "onlypans.common.entity"})
public class UserService {

    private RestTemplate restTemplate;
    @Autowired
    private HttpServletRequest httpServletRequest;

    public UserService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {
        try {
            userRepository.save(user);
            return userRepository.save(user);

        } catch (Exception e) {
            throw new UnableToCreateResourceException("Unable to create User", e);
        }
    }


    public Optional<User> getUserById(String id) {
        try {
            return userRepository.findById(id);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Unable to find User with ID " + id);
        }
    }

    public User getUserByEmail(String email) {
        System.out.println("Received email: " + email);
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));
    }

    public List<User> getAllUsers() {
        try {
            return userRepository.findAll();
        } catch (Exception e) {
            throw new ResourceNotFoundException("Unable to find all Users");
        }
    }

    public User updateUser(String id, User userDetails) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with ID " + id));
        user.setUsername(userDetails.getUsername());
        user.setFirstName(userDetails.getFirstName());
        user.setLastName(userDetails.getLastName());
        user.setEmail(userDetails.getEmail());

        return userRepository.save(user);

    }


    public void deleteUser(String id) { // make this delete an account and the creatorprofile when a user is deleted!!
        try {
            userRepository.deleteById(id);
        } catch (Exception e) {
            throw new UnableToDeleteResourceException("Unable to delete user with ID " + id, e);
        }
    }

    public void upgradeToCreatorProfile(CreatorProfileRequest request) {
        // Fetch the user by ID
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String authToken = httpServletRequest.getHeader("Authorization");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authToken);

            // Call CreatorService to create a creator profile using REST API
            String creatorServiceUrl = "http://creator-service/creator-profiles/create";

            HttpEntity<CreatorProfileRequest> entity = new HttpEntity<>(request, headers);
            restTemplate.postForObject(creatorServiceUrl, entity, String.class);
        }

}




