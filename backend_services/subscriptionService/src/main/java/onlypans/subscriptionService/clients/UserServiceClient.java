package onlypans.subscriptionService.clients;

import onlypans.subscriptionService.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "http://user-service:8080", configuration = FeignTokenForwardingConfig.class)
public interface UserServiceClient {
    @GetMapping("/users/{id}")
    User getUserById(@PathVariable String id);
}