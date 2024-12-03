package onlypans.creatorService.clients;

import onlypans.common.dtos.CreatePriceRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "subscription-service", url = "http://subscription-service:8080", configuration = FeignTokenForwardingConfig.class)
public interface SubscriptionServiceClient {
    @PostMapping("/subscriptions/createPrice")
    String createPrice(@RequestBody CreatePriceRequest request);
}