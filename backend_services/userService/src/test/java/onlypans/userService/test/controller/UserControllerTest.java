

package onlypans.userService.test.controller;

import onlypans.common.dtos.CreatorProfileRequest;
import onlypans.userService.controller.UserController;
import onlypans.userService.service.UserService;
import onlypans.common.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUser() throws Exception {
        User requestUser = new User();
        requestUser.setUsername("testUser");

        User createdUser = new User();
        createdUser.setId("1");
        createdUser.setUsername("testUser");

        when(userService.createUser(any(User.class))).thenReturn(createdUser);

        User responseUser = userController.createUser(requestUser).getBody();

        assertNotNull(responseUser);
        assertEquals("1", responseUser.getId());
        assertEquals("testUser", responseUser.getUsername());
    }

    @Test
    void testGetUserById() {
        User user = new User();
        user.setId("1");
        user.setUsername("testUser");

        when(userService.getUserById(eq("1"))).thenReturn(Optional.of(user));

        User responseUser = userController.getUserById("1").get();

        assertNotNull(responseUser);
        assertEquals("1", responseUser.getId());
        assertEquals("testUser", responseUser.getUsername());
    }

    @Test
    void testGetAllUsers() {
        User user = new User();
        user.setId("1");
        user.setUsername("testUser");

        when(userService.getAllUsers()).thenReturn(Collections.singletonList(user));

        var users = userController.getAllUsers();

        assertEquals(1, users.size());
        assertEquals("testUser", users.get(0).getUsername());
    }

    @Test
    void testUpgradeToCreatorProfile() throws Exception {
        Jwt jwt = Jwt.withTokenValue("mock-token")
                .header("alg", "none")
                .claim("sub", "user123")
                .claim("given_name", "John")
                .claim("family_name", "Doe")
                .build();

        JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(jwt);

        when(userService.getUserById(eq("user123"))).thenReturn(Optional.of(new User()));

        var response = userController.upgradeToCreatorProfile(new CreatorProfileRequest(), authenticationToken);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("User upgraded to creator profile", response.getBody());
    }

    @Test
    void testUpgradeToCreatorProfileUserNotFound() throws Exception {
        Jwt jwt = Jwt.withTokenValue("mock-token")
                .header("alg", "none")
                .claim("sub", "user123")
                .claim("given_name", "John")
                .claim("family_name", "Doe")
                .build();

        JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(jwt);

        when(userService.getUserById(eq("user123"))).thenReturn(Optional.empty());

        var response = userController.upgradeToCreatorProfile(new CreatorProfileRequest(), authenticationToken);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("User not found", response.getBody());
    }
}
