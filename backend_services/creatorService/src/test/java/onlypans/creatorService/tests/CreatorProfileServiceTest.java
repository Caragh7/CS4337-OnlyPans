package onlypans.creatorService.tests;

import onlypans.common.dtos.CreatePriceRequest;
import onlypans.common.dtos.CreatorProfileRequest;
import onlypans.common.entity.CreatorProfile;
import onlypans.common.exceptions.*;
import onlypans.creatorService.clients.SubscriptionServiceClient;
import onlypans.creatorService.repository.CreatorProfileRepository;
import onlypans.creatorService.service.CreatorProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreatorProfileServiceTest {

    @InjectMocks
    private CreatorProfileService creatorProfileService;

    @Mock
    private CreatorProfileRepository creatorProfileRepository;

    @Mock
    private SubscriptionServiceClient subscriptionServiceClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateCreatorProfile_Success() {
        CreatorProfileRequest request = new CreatorProfileRequest();
        request.setUserId("user123");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setPrice(String.valueOf(9.99));

        when(subscriptionServiceClient.createPrice(any(CreatePriceRequest.class))).thenReturn("price123");
        when(creatorProfileRepository.save(any(CreatorProfile.class))).thenReturn(new CreatorProfile());

        CreatorProfile result = creatorProfileService.createCreatorProfile(request);

        assertNotNull(result);
        verify(subscriptionServiceClient).createPrice(any(CreatePriceRequest.class));
        verify(creatorProfileRepository).save(any(CreatorProfile.class));
    }

    @Test
    void testGetAllCreators() {
        when(creatorProfileRepository.findAll()).thenReturn(Arrays.asList(new CreatorProfile(), new CreatorProfile()));

        assertEquals(2, creatorProfileService.getAllCreators().size());
        verify(creatorProfileRepository).findAll();
    }

    @Test
    void testGetCreatorProfileById_Found() {
        CreatorProfile profile = new CreatorProfile();
        when(creatorProfileRepository.findById(1L)).thenReturn(Optional.of(profile));

        Optional<CreatorProfile> result = creatorProfileService.getCreatorProfileById(1L);

        assertTrue(result.isPresent());
        verify(creatorProfileRepository).findById(1L);
    }

    @Test
    void testGetCreatorProfileById_NotFound() {
        when(creatorProfileRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<CreatorProfile> result = creatorProfileService.getCreatorProfileById(1L);

        assertTrue(result.isEmpty(), "Expected the result to be empty when no profile is found");
        verify(creatorProfileRepository).findById(1L);
    }


    @Test
    void testDeleteCreatorProfile_Success() {
        doNothing().when(creatorProfileRepository).deleteByUserId("user123");

        assertDoesNotThrow(() -> creatorProfileService.deleteCreatorProfile("user123"));
        verify(creatorProfileRepository).deleteByUserId("user123");
    }
}
