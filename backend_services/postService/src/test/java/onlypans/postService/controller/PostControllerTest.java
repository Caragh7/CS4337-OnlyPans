package onlypans.postService.controller;

import onlypans.postService.entity.Post;
import onlypans.postService.security.TestSecurityConfig;
import onlypans.postService.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@Import(TestSecurityConfig.class)
@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:test",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "media.service.url=http://localhost:8082",
        "spring.cloud.consul.enabled=false",
        "spring.cloud.consul.discovery.enabled=false",
        "spring.cloud.consul.config.enabled=false"
})
@AutoConfigureMockMvc
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    @MockBean
    private JwtDecoder jwtDecoder;

    @Test
    @WithMockUser(username = "user123")
    void testGetPostById() throws Exception {
        Post post = new Post();
        post.setId(1L);
        post.setContentDescription("Test Description");
        post.setAuthorName("user123");

        when(postService.getPostById(1L)).thenReturn(java.util.Optional.of(post));

        mockMvc.perform(get("/posts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.contentDescription").value("Test Description"))
                .andExpect(jsonPath("$.authorName").value("user123"));

        verify(postService, times(1)).getPostById(1L);
    }

    @Test
    @WithMockUser(username = "user123")
    void testGetPostByIdNotFound() throws Exception {
        when(postService.getPostById(1L)).thenReturn(java.util.Optional.empty());

        mockMvc.perform(get("/posts/1"))
                .andExpect(status().isNotFound());

        verify(postService, times(1)).getPostById(1L);
    }

    @Test
    @WithMockUser(username = "user123")
    void testGetAllPosts() throws Exception {
        mockMvc.perform(get("/posts"))
                .andExpect(status().isOk());

        verify(postService, times(1)).getAllPosts();
    }
}