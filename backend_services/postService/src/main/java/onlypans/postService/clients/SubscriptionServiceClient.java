package onlypans.postService.clients;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "subscription-service", url = "http://subscription-service:8080", configuration = FeignTokenForwardingConfig.class)
public interface SubscriptionServiceClient {

    @GetMapping("/subscriptions/creator-ids")
    List<Long> getCreatorProfileIdsForUser(@RequestParam("userId") String userId);
}
