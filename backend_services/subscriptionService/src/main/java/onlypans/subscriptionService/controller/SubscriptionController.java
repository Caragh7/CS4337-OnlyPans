package onlypans.subscriptionService.controller;

import com.stripe.Stripe;
import com.stripe.StripeClient;
import com.stripe.exception.StripeException;
import com.stripe.model.Product;
import com.stripe.model.StripeCollection;
import com.stripe.model.checkout.Session;
import onlypans.subscriptionService.entity.Subscription;
import onlypans.subscriptionService.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/subscriptions")
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @Autowired
    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @GetMapping("/test")
    public String sendTest() throws StripeException {
        return "Test";
    }

    @GetMapping("/subscribe/{otherUserId}")
    public String createSession(@PathVariable String otherUserId, Authentication authentication) throws Exception {
        String userId = authentication.getName();
        List<Subscription> subscriptions = subscriptionService.getUserSubscriptions(userId);
        System.out.println("Subscription pending for: " + userId + " length: " + subscriptions.size());
        Session createCheckoutSession = subscriptionService.createCheckoutSession("ubdsbds23", otherUserId, "2");
        return createCheckoutSession.getId();
    }
}

