package onlypans.subscriptionService.service;

import com.stripe.Stripe;
import com.stripe.StripeClient;
import com.stripe.model.Product;
import com.stripe.model.checkout.Session;
import com.stripe.param.PriceCreateParams;
import com.stripe.param.ProductRetrieveParams;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionService {
    StripeClient client;
    public SubscriptionService(@Value("${stripe.sk}") String key) {

        Stripe.apiKey = key;
        client = new StripeClient(key);
    }

    public Session createCheckoutSession(String priceId, String userId, String subscriptionId) throws Exception {
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

        Session session = Session.create(params);

        return session;
    }
}