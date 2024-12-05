package onlypans.postService.clients;

import onlypans.common.entity.CreatorProfile;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "creator-service", url = "http://creator-service:8080", configuration = FeignTokenForwardingConfig.class)
public interface CreatorServiceClient {

    @GetMapping("/creator-profiles/user")
    CreatorProfile getCreatorProfileByUserId(@RequestParam("userId") String userId);
}
