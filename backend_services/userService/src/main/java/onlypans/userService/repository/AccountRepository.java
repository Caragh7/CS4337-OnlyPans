package onlypans.userService.repository;

import onlypans.userService.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    // Custom query methods (if needed) can be added here
}
