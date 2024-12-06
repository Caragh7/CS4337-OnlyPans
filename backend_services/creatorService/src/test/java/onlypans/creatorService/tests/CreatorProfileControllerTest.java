package onlypans.creatorService.tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import onlypans.common.dtos.CreatorProfileRequest;
import onlypans.common.entity.CreatorProfile;
import onlypans.creatorService.controller.CreatorProfileController;
import onlypans.creatorService.service.CreatorProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CreatorProfileController.class)
@WithMockUser(username = "testUser", roles = {"USER"})
class CreatorProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean // Use @MockBean instead of @Mock to register the mock with the Spring context
    private CreatorProfileService creatorProfileService;

    @Test
    void testGetAllCreatorProfiles() throws Exception {
        when(creatorProfileService.getAllCreators()).thenReturn(Arrays.asList(new CreatorProfile(), new CreatorProfile()));

        mockMvc.perform(get("/creator-profiles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(creatorProfileService).getAllCreators();
    }

    @Test
    void testCreateCreatorProfile() throws Exception {
        CreatorProfileRequest request = new CreatorProfileRequest();
        request.setUserId("user123");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setPrice("9.99");

        CreatorProfile profile = new CreatorProfile();
        when(creatorProfileService.createCreatorProfile(any(CreatorProfileRequest.class))).thenReturn(profile);

        mockMvc.perform(post("/creator-profiles/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request))
                        .with(jwt().authorities(() -> "ROLE_USER"))) // Simulate JWT authentication
                .andExpect(status().isCreated());

        verify(creatorProfileService).createCreatorProfile(any(CreatorProfileRequest.class));
    }


    @Test
    void testDeleteCreatorProfile() throws Exception {
        doNothing().when(creatorProfileService).deleteCreatorProfile(anyString());

        mockMvc.perform(delete("/creator-profiles/delete")
                        .with(jwt().authorities(() -> "USER"))) // Simulate JWT
                .andExpect(status().isNoContent());
    }
}
