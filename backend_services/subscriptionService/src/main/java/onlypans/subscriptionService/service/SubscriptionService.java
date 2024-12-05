package onlypans.subscriptionService.service;

import com.stripe.Stripe;
import com.stripe.StripeClient;
import com.stripe.exception.StripeException;
import com.stripe.model.Price;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.param.PriceCreateParams;
import com.stripe.param.ProductCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import onlypans.common.dtos.CreateSubscriptionRequest;
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
import java.util.Map;
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

    public Optional<Subscription> getSubscription(Long subscriptionId) {
        return this.subscriptionRepository.findById(subscriptionId);
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

    public Session createCheckoutSession(String priceId, CreateSubscriptionRequest request, Subscription subscription) throws Exception {
        if (!checkUserExists(request.getUserId())) {
            throw new NoSuchElementException("User with ID " + request.getUserId() + " does not exist.");
        }

        SessionCreateParams params = SessionCreateParams.builder()
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setPrice(priceId)
                        .setQuantity(1L)
                        .build())
                .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                .setSuccessUrl(request.getFrom() + "/success?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl(request.getFrom() + "/")
                .putMetadata("userId", request.getUserId())
                .putMetadata("subscriptionId", String.valueOf(subscription.getId()))
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

    public Subscription createSubscription(CreateSubscriptionRequest request, String userId, Long creatorId) {
        Subscription subscription = new Subscription();
        subscription.setState("PENDING");
        subscription.setUserId(request.getUserId());
        subscription.setUserId(userId);
        subscription.setCreatorProfileId(creatorId);

        subscriptionRepository.save(subscription);
        return subscription;
    }

    public Optional<User> getUserById(String userId) {
        return Optional.ofNullable(userServiceClient.getUserById(userId));
    }

    public Optional<CreatorProfile> getCreatorProfileByUserId(String userId) {
        return creatorServiceClient.getCreatorProfile(userId);
    }

    public List<Long> getCreatorProfileIdsForUser(String userId) {
        return subscriptionRepository.findCreatorProfileIdsByUserId(userId);
    }

    public void handleCheckoutSessionCompleted(StripeObject stripeObject) {
        if (stripeObject instanceof com.stripe.model.checkout.Session session) {
            String subscriptionId = session.getSubscription();
            Map<String, String> metadata = session.getMetadata();

            if (metadata != null) {
                String subscriptionIdMeta = metadata.get("subscriptionId");
                String userId = metadata.get("userId");

                Optional<Subscription> optionalSubscription = getSubscription(Long.valueOf(subscriptionIdMeta));
                if (optionalSubscription.isPresent()) {
                    Subscription subscription = optionalSubscription.get();
                    subscription.setStripeSubscriptionId(subscriptionId);
                    subscription.setState("ACTIVE");
                    subscriptionRepository.save(subscription);
                    System.out.println("Subscription updated successfully.");
                } else {
                    System.out.println("Subscription not found for ID: " + subscriptionIdMeta);
                }
            } else {
                System.out.println("No metadata found in session.");
            }
        } else {
            System.out.println("Stripe object is not a Session.");
        }
    }

    public void handleSubscriptionDeleted(StripeObject stripeObject) {
        if (stripeObject instanceof com.stripe.model.Subscription subscription) {
//            String stripeSubscriptionId = subscription.getId();
//            List<Subscription> subscriptions = getSubscriptionsByStripeId(stripeSubscriptionId);
//            subscriptions.forEach(sub -> {
//                sub.setState("CANCELED");
//                subscriptionService.saveSubscription(sub);
//            });
//            System.out.println("Subscription(s) canceled successfully for Stripe ID: " + stripeSubscriptionId);
        } else {
            System.out.println("Stripe object is not a Subscription.");
        }
    }


}