package onlypans.creatorService.clients;

import onlypans.common.dtos.CreatePriceRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "subscription-service", url = "http://subscription-service:8080", configuration = FeignTokenForwardingConfig.class)
public interface SubscriptionServiceClient {
    @PostMapping("/subscriptions/createPrice")
    String createPrice(@RequestBody CreatePriceRequest request);

    @GetMapping("/subscriptions/creator-ids")
    List<Long> getCreatorProfileIdsForUser(@RequestParam("userId") String userId);
}
