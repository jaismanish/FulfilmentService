package FulfilmentService.services;

import FulfilmentService.dto.Address;
import FulfilmentService.exceptions.OrderAlreadyProcessedException;
import FulfilmentService.models.DeliveryRequest;
import FulfilmentService.repositories.DeliveryRepository;
import FulfilmentService.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class DeliveryServiceTest {

    @Mock
    private DeliveryRepository deliveryRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private DeliveryService deliveryService;

    @BeforeEach
    void setup() {
        openMocks(this);
    }
    @Test
    void testDeliveryAlreadyProcessed_throwsException() {
        Address address = Address.builder()
                .building(1)
                .floor("floor")
                .locality("locality")
                .city("city")
                .state("state")
                .country("country")
                .pinCode("560068")
                .build();
        long orderId = 1L;
        DeliveryRequest request = DeliveryRequest.builder()
                .orderId(orderId)
                .pickupAddress(address)
                .build();

        when(deliveryRepository.isPresentByOrderId(orderId)).thenReturn(true);

        assertThrows(OrderAlreadyProcessedException.class, () -> deliveryService.process(request));
        verify(userRepository, never()).findAll();
    }
}