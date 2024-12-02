package onlypans.subscriptionService.service;

import com.stripe.Stripe;
import com.stripe.StripeClient;
import com.stripe.model.Product;
import com.stripe.model.checkout.Session;
import com.stripe.param.PriceCreateParams;
import com.stripe.param.ProductRetrieveParams;
import com.stripe.param.checkout.SessionCreateParams;
import onlypans.subscriptionService.clients.UserServiceClient;
import onlypans.subscriptionService.entity.Subscription;
import onlypans.subscriptionService.entity.User;
import onlypans.subscriptionService.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class SubscriptionService {
    private final StripeClient client;
    private final SubscriptionRepository subscriptionRepository;
    private final UserServiceClient userServiceClient;

    @Autowired
    public SubscriptionService(
            @Value("${stripe.sk}") String key,
            SubscriptionRepository subscriptionRepository,
            UserServiceClient userServiceClient
    ) {
        Stripe.apiKey = key;
        this.client = new StripeClient(key);
        this.subscriptionRepository = subscriptionRepository;
        this.userServiceClient = userServiceClient;
    }

    public boolean checkUserExists(String userId) {
        try {
            User user = userServiceClient.getUserById(userId);
            System.out.println(user.getEmail());
            return user != null;
        } catch (Exception e) {
            // Log the exception and handle it appropriately
            System.err.println("Error checking user existence: " + e.getMessage());
            return false;
        }
    }

    public Session createCheckoutSession(String priceId, String userId, String subscriptionId) throws Exception {
        if (!checkUserExists(userId)) {
            throw new NoSuchElementException("User with ID " + userId + " does not exist.");
        }

        SessionCreateParams params = SessionCreateParams.builder()
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setPrice("price_1QKMgmAH1RB2Ujn9UFsA5Ozb")
                        .setQuantity(1L)
                        .build())
                .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                .setSuccessUrl("https://yourdomain.com/success?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl("https://yourdomain.com/cancel")
                .putMetadata("userId", userId)
                .putMetadata("subscriptionId", subscriptionId)
                .build();

        return Session.create(params);
    }

    public List<Subscription> getUserSubscriptions(String userId) throws NoSuchElementException {
        return subscriptionRepository.findByUserId(userId);
    }
}