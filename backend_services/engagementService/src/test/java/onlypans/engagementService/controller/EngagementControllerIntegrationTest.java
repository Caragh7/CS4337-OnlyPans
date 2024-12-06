package onlypans.engagementService.controller;

import jakarta.transaction.Transactional;
import onlypans.engagementService.entity.Comments;
import onlypans.engagementService.entity.Likes;
import onlypans.engagementService.security.TestSecurityConfig;
import onlypans.engagementService.service.EngagementService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jwt.JwtDecoder;




// We do not need consul for our tests
@SpringBootTest(properties = {
        "spring.cloud.consul.config.enabled=false",
        "spring.cloud.consul.discovery.enabled=false",
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.jpa.hibernate.ddl-auto=create-drop"
}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ImportAutoConfiguration(exclude = {
        org.springframework.cloud.consul.serviceregistry.ConsulAutoServiceRegistrationAutoConfiguration.class,
        org.springframework.cloud.consul.serviceregistry.ConsulServiceRegistryAutoConfiguration.class
})
@AutoConfigureMockMvc
@Transactional
@Import(TestSecurityConfig.class)
class EngagementControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "user123")
    void testLikePost() throws Exception {
        mockMvc.perform(post("/engagements/likes")
                        .param("postId", "1")
                        .with(csrf()))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "user123")
    void testCommentOnPost() throws Exception {
        mockMvc.perform(post("/engagements/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"postId\":1,\"commentBody\":\"wow this looks yum\"}")
                        .with(csrf()))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "user123")
    void testGetCommentsByPost() throws Exception {
        mockMvc.perform(post("/engagements/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"postId\":1,\"commentBody\":\"wow this looks yum\"}")
                        .with(csrf()))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/engagements/comments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].text").value("wow this looks yum"));
    }
}
