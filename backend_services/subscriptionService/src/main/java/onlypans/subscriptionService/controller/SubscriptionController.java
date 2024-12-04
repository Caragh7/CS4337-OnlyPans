package onlypans.subscriptionService.controller;

import com.stripe.Stripe;
import com.stripe.StripeClient;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.*;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import onlypans.common.dtos.CreatePriceRequest;
import onlypans.common.dtos.CreateSubscriptionRequest;
import onlypans.common.entity.User;
import onlypans.common.entity.CreatorProfile;
import onlypans.subscriptionService.entity.Subscription;
import onlypans.subscriptionService.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/subscriptions")
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @Autowired
    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @PostMapping("/createPrice")
    public String createPrice(@RequestBody CreatePriceRequest request) throws StripeException {
        float price = Float.parseFloat(request.getPrice());
        int stripePrice = Math.round(price * 100);
        Price createdPrice = subscriptionService.createPrice(stripePrice, request.getCreatorName());

        System.out.println("Price Created! " + createdPrice.getId() + " " + createdPrice.getNickname());
        return createdPrice.getId();
    }

    @GetMapping("/test")
    public String get() {
        return "Test";
    }

    @PostMapping("/subscribe")
    public String createSession(@RequestBody CreateSubscriptionRequest request, Authentication authentication) throws Exception {
        String userId = authentication.getName();
        Optional<User> getCreator = subscriptionService.getUserById(request.getUserId());
        if(getCreator.isEmpty()) return "Error: User not found";

        Optional<CreatorProfile> creator = subscriptionService.getCreatorProfileByUserId(request.getUserId());
        if(creator.isEmpty()) return "Error: Creator not found";

        Subscription subscription = subscriptionService.createSubscription(request, userId, creator.get().getId());
        Session createCheckoutSession = subscriptionService.createCheckoutSession(creator.get().getStripePriceId(), request, subscription);
        return createCheckoutSession.getId();
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> webhook(
            @Value("${stripe.whsec}") String key,
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {
        Event event;
        try {
            event = Webhook.constructEvent(payload, sigHeader, key);
        } catch (SignatureVerificationException e) {
            System.out.println("Failed signature verification");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid signature");
        }

        EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
        StripeObject stripeObject = dataObjectDeserializer.getObject().orElse(null);

        if (stripeObject == null) {
            System.out.println("Unable to decode event data");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid event data");
        }

        switch (event.getType()) {
            case "checkout.session.completed": {
                subscriptionService.handleCheckoutSessionCompleted(stripeObject);
                break;
            }
            case "customer.subscription.deleted": {
                subscriptionService.handleSubscriptionDeleted(stripeObject);
                break;
            }
            default:
                System.out.println("Unhandled event type: " + event.getType());
        }

        return ResponseEntity.ok("Success");
    }

}

