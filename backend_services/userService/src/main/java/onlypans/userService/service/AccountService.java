package onlypans.userService.service;

import onlypans.userService.entity.Account;
import onlypans.userService.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    // Create a new account associated with a user
    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    // Retrieve account information by user ID
    public Optional<Account> getAccountByUserId(Long userId) {
        return accountRepository.findByUserId(userId);
    }


//    // Refresh the access token for a user's account
//    public Account refreshAccessToken(String userId, String newAccessToken) {
//        Optional<Account> accountOpt = accountRepository.findByUserId(userId);
//        if (accountOpt.isPresent()) {
//            Account account = accountOpt.get();
//            account.setAccessToken(newAccessToken);
//            return accountRepository.save(account);
//        } else {
//            throw new RuntimeException("Account not found for userId: " + userId);
//        }
//    }
//
//    // Update refresh token for a user's account
//    public Account updateRefreshToken(String userId, String newRefreshToken) {
//        Optional<Account> accountOpt = accountRepository.findByUserId(userId);
//        if (accountOpt.isPresent()) {
//            Account account = accountOpt.get();
//            account.setRefreshToken(newRefreshToken);
//            return accountRepository.save(account);
//        } else {
//            throw new RuntimeException("Account not found for userId: " + userId);
//        }
//    }

    // Delete account associated with a user
    public void deleteAccountByUserId(Long userId) {
        Optional<Account> accountById = accountRepository.findByUserId(userId);
        accountById.ifPresent(accountRepository::delete);
    }
}
