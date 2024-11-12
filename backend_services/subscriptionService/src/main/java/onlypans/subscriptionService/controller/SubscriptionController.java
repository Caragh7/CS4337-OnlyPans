package onlypans.subscriptionService.controller;

import com.stripe.Stripe;
import com.stripe.StripeClient;
import com.stripe.exception.StripeException;
import com.stripe.model.Product;
import com.stripe.model.StripeCollection;
import com.stripe.model.checkout.Session;
import onlypans.subscriptionService.service.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/subscriptions")
public class SubscriptionController {
    @Autowired
    private SubscriptionService subscriptionService;

    @GetMapping("/test")
    public String sendTest() throws StripeException {
        return "Test";
    }

    @GetMapping("/payment/{otherUserId}")
    public String createSession(@PathVariable String otherUserId) throws Exception {
        Session createCheckoutSession = subscriptionService.createCheckoutSession("ubdsbds23", otherUserId, "2");
        return createCheckoutSession.getId();
    }
}

