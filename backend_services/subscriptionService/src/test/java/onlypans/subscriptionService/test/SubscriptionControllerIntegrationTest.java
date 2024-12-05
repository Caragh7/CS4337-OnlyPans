package onlypans.subscriptionService.test;

import onlypans.subscriptionService.entity.Subscription;
import onlypans.subscriptionService.repository.SubscriptionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SubscriptionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Test
    @WithMockUser(username = "user123", roles = {"USER"})
    void testGetAllSubscriptions() throws Exception {
        Subscription subscription = new Subscription();
        subscription.setUserId("user123");
        subscription.setState("ACTIVE");
        subscriptionRepository.save(subscription);

        mockMvc.perform(get("/subscriptions/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].userId").value("user123"))
                .andExpect(jsonPath("$[0].state").value("ACTIVE"));

        subscriptionRepository.deleteAll();
    }
}
