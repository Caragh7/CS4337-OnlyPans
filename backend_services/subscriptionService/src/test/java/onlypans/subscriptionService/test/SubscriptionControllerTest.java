package onlypans.subscriptionService.test;

import com.stripe.model.Price;
import onlypans.common.dtos.CreatePriceRequest;
import onlypans.common.dtos.CreateSubscriptionRequest;
import onlypans.subscriptionService.controller.SubscriptionController;
import onlypans.subscriptionService.entity.Subscription;
import onlypans.subscriptionService.service.SubscriptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class SubscriptionControllerTest {

    @Mock
    private SubscriptionService subscriptionService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private SubscriptionController subscriptionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreatePrice() throws Exception {
        CreatePriceRequest request = new CreatePriceRequest("1", "9.99", "CreatorName");
        Price mockPrice = mock(Price.class);
        when(mockPrice.getId()).thenReturn("price_123");

        when(subscriptionService.createPrice(999, "CreatorName")).thenReturn(mockPrice);

        String priceId = subscriptionController.createPrice(request);

        assertEquals("price_123", priceId);
        verify(subscriptionService, times(1)).createPrice(999, "CreatorName");
    }

    @Test
    void testGetAllSubscriptions() {
        when(authentication.getName()).thenReturn("user123");
        when(subscriptionService.getUserSubscriptions("user123")).thenReturn(Collections.emptyList());

        List<Subscription> subscriptions = subscriptionController.getAll(authentication);

        assertEquals(0, subscriptions.size());
        verify(subscriptionService, times(1)).getUserSubscriptions("user123");
    }

    @Test
    void testCreateSession() throws Exception {
        CreateSubscriptionRequest request = new CreateSubscriptionRequest();
        request.setUserId("creator123");
        when(authentication.getName()).thenReturn("user123");
        when(subscriptionService.getUserById("creator123")).thenReturn(Optional.empty());

        String response = subscriptionController.createSession(request, authentication);

        assertEquals("Error: User not found", response);
        verify(subscriptionService, times(1)).getUserById("creator123");
    }

    @Test
    void testWebhookInvalidSignature() {
        String payload = "{}";
        String signature = "invalid_signature";
        String secret = "whsec_test";

        ResponseEntity<String> response = subscriptionController.webhook(secret, payload, signature);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid signature", response.getBody());
    }
}
