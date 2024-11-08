package onlypans.userService.service;

import onlypans.userService.entity.Account;
import onlypans.userService.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Optional<Account> findByUserId(Long userId) {
        return accountRepository.findById(userId);
    }

    public Account saveAccount(Account account) {
        return accountRepository.save(account);
    }

    // Additional methods for updating tokens, finding by tokens, etc.
}
