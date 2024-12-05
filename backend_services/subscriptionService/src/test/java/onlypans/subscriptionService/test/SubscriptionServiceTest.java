package onlypans.subscriptionService.test;

import com.stripe.StripeClient;
import com.stripe.exception.StripeException;
import com.stripe.model.Price;
import com.stripe.model.Product;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.param.PriceCreateParams;
import com.stripe.param.ProductCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.service.CheckoutService;
import com.stripe.service.PriceService;
import com.stripe.service.ProductService;
import com.stripe.service.checkout.SessionService;
import onlypans.common.dtos.CreateSubscriptionRequest;
import onlypans.common.entity.User;
import onlypans.subscriptionService.clients.CreatorServiceClient;
import onlypans.subscriptionService.clients.UserServiceClient;
import onlypans.common.entity.CreatorProfile;
import onlypans.subscriptionService.entity.Subscription;
import onlypans.subscriptionService.repository.SubscriptionRepository;
import onlypans.subscriptionService.service.SubscriptionService;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class SubscriptionServiceTest {

    @Autowired
    private SubscriptionService subscriptionService;

    @MockBean
    private SubscriptionRepository subscriptionRepository;

    @MockBean
    private UserServiceClient userServiceClient;

    @MockBean
    private CreatorServiceClient creatorServiceClient;

    @Test
    void testGetSubscription() {
        Subscription subscription = new Subscription();
        subscription.setId(1L);

        when(subscriptionRepository.findById(1L)).thenReturn(Optional.of(subscription));

        Optional<Subscription> result = subscriptionService.getSubscription(1L);

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
    }

    @Test
    void testCheckUserExists_UserExists() {
        String userId = "user123";
        User mockUser = new User();
        mockUser.setId(userId);
        mockUser.setEmail("test@example.com");

        when(userServiceClient.getUserById(userId)).thenReturn(mockUser);

        boolean exists = subscriptionService.checkUserExists(userId);

        assertTrue(exists);
    }

    @Test
    void testCheckUserExists_UserDoesNotExist() {
        String userId = "user456";

        when(userServiceClient.getUserById(userId)).thenThrow(new NoSuchElementException("User not found"));

        boolean exists = subscriptionService.checkUserExists(userId);

        assertFalse(exists);
    }

    @Test
    void testCreateCheckoutSession() throws Exception {
        String priceId = "price_123";
        CreateSubscriptionRequest request = new CreateSubscriptionRequest();
        request.setUserId("user123");
        request.setFrom("http://example.com");

        Subscription subscription = new Subscription();
        subscription.setId(1L);

        Session mockSession = mock(Session.class);
        when(mockSession.getId()).thenReturn("mock_session_id");

        MockedStatic<Session> mockedSession = mockStatic(Session.class);
        mockedSession.when(() -> Session.create(any(SessionCreateParams.class))).thenReturn(mockSession);

        when(userServiceClient.getUserById("user123")).thenReturn(new User());

        SubscriptionService service = new SubscriptionService(
                "test_key", subscriptionRepository, userServiceClient, creatorServiceClient);

        Session session = service.createCheckoutSession(priceId, request, subscription);

        assertNotNull(session);
        assertEquals("mock_session_id", session.getId());

        verify(userServiceClient).getUserById("user123");
        mockedSession.verify(() -> Session.create(any(SessionCreateParams.class)), times(1));

        mockedSession.close();
    }


    @Test
    void testGetUserSubscriptions() {
        String userId = "user123";
        Subscription subscription = new Subscription();
        subscription.setUserId(userId);

        when(subscriptionRepository.findByUserId(userId)).thenReturn(List.of(subscription));

        List<Subscription> subscriptions = subscriptionService.getUserSubscriptions(userId);

        assertEquals(1, subscriptions.size());
        assertEquals(userId, subscriptions.get(0).getUserId());
    }

    @Test
    void testCreatePrice() throws StripeException {
        int price = 1000;
        String name = "Test Price";

        PriceService mockPriceService = mock(PriceService.class);
        StripeClient mockClient = mock(StripeClient.class);
        ProductService mockProductService = mock(ProductService.class);

        Price mockPrice = new Price();
        mockPrice.setId("price_123");

        Product mockProduct = new Product();
        mockProduct.setDefaultPrice(mockProduct.getId());
        mockProduct.setName(name);

        when(mockClient.prices()).thenReturn(mockPriceService);
        when(mockClient.products()).thenReturn(mockProductService);
        when(mockPriceService.create(any(PriceCreateParams.class))).thenReturn(mockPrice);
        when(mockProductService.create(any(ProductCreateParams.class))).thenReturn(mockProduct);

        SubscriptionService serviceWithMockClient = new SubscriptionService(
                "test_key", subscriptionRepository, userServiceClient, creatorServiceClient);
        ReflectionTestUtils.setField(serviceWithMockClient, "client", mockClient);

        Price createdPrice = serviceWithMockClient.createPrice(price, name);

        assertEquals("price_123", createdPrice.getId());
    }


    @Test
    void testCreateSubscription() {
        CreateSubscriptionRequest request = new CreateSubscriptionRequest();
        request.setUserId("user123");
        String userId = "user123";
        Long creatorId = 123L;

        Subscription createdSubscription = subscriptionService.createSubscription(request, userId, creatorId);

        assertEquals("user123", createdSubscription.getUserId());
        assertEquals(creatorId, createdSubscription.getCreatorProfileId());
    }

    @Test
    void testHandleCheckoutSessionCompleted() {
        Session session = mock(Session.class);
        when(session.getSubscription()).thenReturn("sub_123");
        when(session.getMetadata()).thenReturn(Map.of("subscriptionId", "1", "userId", "user123"));

        Subscription subscription = new Subscription();
        subscription.setId(1L);

        when(subscriptionRepository.findById(1L)).thenReturn(Optional.of(subscription));

        subscriptionService.handleCheckoutSessionCompleted(session);

        verify(subscriptionRepository, times(1)).save(subscription);
    }


}
