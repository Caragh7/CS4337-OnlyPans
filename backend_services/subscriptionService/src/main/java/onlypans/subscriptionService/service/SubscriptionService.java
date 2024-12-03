package onlypans.subscriptionService.service;

import com.stripe.Stripe;
import com.stripe.StripeClient;
import com.stripe.exception.StripeException;
import com.stripe.model.Price;
import com.stripe.model.Product;
import com.stripe.model.checkout.Session;
import com.stripe.param.PriceCreateParams;
import com.stripe.param.ProductCreateParams;
import com.stripe.param.ProductRetrieveParams;
import com.stripe.param.checkout.SessionCreateParams;
import onlypans.common.entity.User;
import onlypans.subscriptionService.clients.CreatorServiceClient;
import onlypans.subscriptionService.clients.UserServiceClient;
import onlypans.common.entity.CreatorProfile;
import onlypans.subscriptionService.entity.Subscription;
import onlypans.subscriptionService.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class SubscriptionService {
    private final StripeClient client;
    private final SubscriptionRepository subscriptionRepository;
    private final UserServiceClient userServiceClient;
    private final CreatorServiceClient creatorServiceClient;

    @Autowired
    public SubscriptionService(
            @Value("${stripe.sk}") String key,
            SubscriptionRepository subscriptionRepository,
            UserServiceClient userServiceClient,
            CreatorServiceClient creatorServiceClient
    ) {
        Stripe.apiKey = key;
        this.client = new StripeClient(key);
        this.subscriptionRepository = subscriptionRepository;
        this.userServiceClient = userServiceClient;
        this.creatorServiceClient = creatorServiceClient;
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
                        .setPrice(priceId)
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

    public Price createPrice(int price, String name) throws StripeException {

        ProductCreateParams createParams = ProductCreateParams.builder()
                .setName("One Month" + " " + name)
                .setDescription(name)
                .build();

        String productId = this.client.products().create(createParams).getId();

        PriceCreateParams priceParams = PriceCreateParams.builder()
                .setUnitAmount(Long.valueOf(price))
                .setCurrency("EUR")
                .setRecurring(PriceCreateParams.Recurring.builder()
                        .setInterval(PriceCreateParams.Recurring.Interval.MONTH)
                        .build())
                .setNickname(name + (Math.floor(Math.random() * 10000) - 100))
                .setProduct(productId)
                .build();

        return this.client.prices().create(priceParams);
    }

    public Optional<User> getUserById(String userId) {
        return Optional.ofNullable(userServiceClient.getUserById(userId));
    }

    public Optional<CreatorProfile> getCreatorProfileByUserId(String userId) {
        return creatorServiceClient.getCreatorProfile(userId);
    }
}