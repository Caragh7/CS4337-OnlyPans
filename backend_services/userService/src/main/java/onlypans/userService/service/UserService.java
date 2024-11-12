package onlypans.userService.service;

import onlypans.common.dtos.CreatorProfileRequest;
import onlypans.common.exceptions.*;
import onlypans.userService.entity.Account;
import onlypans.userService.entity.User;
import onlypans.userService.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private RestTemplate restTemplate;
    public UserService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountService accountService;



    public User createUser(User user) {
        try {
            userRepository.save(user);
            Account account = new Account();
            account.setUser(user);
            accountService.createAccount(account);
            return userRepository.save(user);

        } catch (Exception e) {
            throw new UnableToCreateResourceException("Unable to create User", e);
        }
    }


    public Optional<User> getUserById(Long id) {
        try {
            return userRepository.findById(id);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Unable to find User with ID " + id);
        }
    }

    public List<User> getAllUsers() {
        try {
            return userRepository.findAll();
        } catch (Exception e) {
            throw new ResourceNotFoundException("Unable to find all Users");
        }
    }

    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with ID " + id));
        user.setUsername(userDetails.getUsername());
        user.setEmail(userDetails.getEmail());
        user.setPassword(userDetails.getPassword());
        return userRepository.save(user);
    }


    public void deleteUser(Long id) { // make this delete an account and the creatorprofile when a user is deleted!!
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


            // Call CreatorService to create a creator profile using REST API
            String creatorServiceUrl = "http://creator-service/creator-profiles/create";
            restTemplate.postForObject(creatorServiceUrl, request, String.class);
        }

}




