package onlypans.subscriptionService.clients;

import onlypans.common.entity.CreatorProfile;
import onlypans.common.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(name = "creator-service", url = "http://creator-service:8080", configuration = FeignTokenForwardingConfig.class)
public interface CreatorServiceClient {
    @GetMapping("/creator-profiles/user/{id}")
    Optional<CreatorProfile> getCreatorProfile(@PathVariable String id);
}