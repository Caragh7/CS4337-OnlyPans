package onlypans.engagementService.clients;

import onlypans.common.entity.User;
import onlypans.engagementService.clients.FeignTokenForwardingConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "http://user-service:8080", configuration = FeignTokenForwardingConfig.class)
public interface UserServiceClient {
    @GetMapping("/users/{id}")
    User getUserById(@PathVariable String id);
}