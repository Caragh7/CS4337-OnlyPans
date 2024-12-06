package onlypans.userService.test.service;

import jakarta.servlet.http.HttpServletRequest;
import onlypans.common.entity.User;
import onlypans.userService.repository.UserRepository;
import onlypans.userService.service.UserService;
import onlypans.userService.test.security.TestSecurityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:test",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.cloud.consul.enabled=false",
        "spring.cloud.consul.discovery.enabled=false",
        "spring.cloud.consul.config.enabled=false"
})
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
class UserServiceIntegrationTest {


    @MockBean
    private RestTemplate restTemplate;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private JwtDecoder jwtDecoder;


    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        // Mock repository methods
        User user = new User();
        user.setId("1");
        user.setUsername("testUser");

        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(userRepository.findById("1")).thenReturn(Optional.of(user));
        when(userRepository.findAll()).thenReturn(List.of(user));
    }

    @Test
    @WithMockUser(username = "user123", roles = {"USER"})
    void testCreateUser() {
        User user = new User();
        user.setId("1");
        user.setUsername("newUser");

        User createdUser = userService.createUser(user);

        assertNotNull(createdUser);
        assertEquals("newUser", createdUser.getUsername());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    @WithMockUser(username = "user123", roles = {"USER"})
    void testGetUserById() {
        Optional<User> user = userService.getUserById("1");

        assertTrue(user.isPresent());
        assertEquals("testUser", user.get().getUsername());
        verify(userRepository, times(1)).findById("1");
    }

    @Test
    @WithMockUser(username = "user123", roles = {"USER"})
    void testGetAllUsers() {
        List<User> users = userService.getAllUsers();

        assertFalse(users.isEmpty());
        assertEquals(1, users.size());
        assertEquals("testUser", users.get(0).getUsername());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    @WithMockUser(username = "user123", roles = {"USER"})
    void testUpdateUser() {
        User updatedDetails = new User();
        updatedDetails.setUsername("updatedUser");

        User updatedUser = userService.updateUser("1", updatedDetails);

        assertNotNull(updatedUser);
        assertEquals("updatedUser", updatedUser.getUsername());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @WithMockUser(username = "user123", roles = {"USER"})
    void testDeleteUser() {
        userService.deleteUser("1");

        verify(userRepository, times(1)).deleteById("1");
    }
}
