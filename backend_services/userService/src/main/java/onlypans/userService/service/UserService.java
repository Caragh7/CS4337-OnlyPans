package onlypans.userService.service;

import onlypans.common.exceptions.*;
import onlypans.userService.entity.Account;
import onlypans.userService.entity.User;
import onlypans.userService.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountService accountService;

    public User createUser(User user) {
        try {
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


    public void deleteUser(Long id) {
        try {
            userRepository.deleteById(id);
        } catch (Exception e) {
            throw new UnableToDeleteResourceException("Unable to delete user with ID " + id, e);
        }
    }
}
