package FulfilmentService.services;

import FulfilmentService.dto.Address;
import FulfilmentService.dto.ApiResponse;
import FulfilmentService.exceptions.UserAlreadyRegistered;
import FulfilmentService.models.RegistrationRequest;
import FulfilmentService.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    Address address = new Address();
    RegistrationRequest registrationRequest = new RegistrationRequest();
    @BeforeEach
    void setup() {
        openMocks(this);
        reset(userService);
        address = Address.builder()
                .building(1)
                .floor("floor")
                .locality("locality")
                .city("city")
                .state("state")
                .country("country")
                .pinCode("560068")
                .build();

        registrationRequest = RegistrationRequest.builder()
                .name("name")
                .username("username")
                .password("password")
                .address(address)
                .build();
    }

    @Test
    void testRegisterUserSuccessfully() {
        when(userRepository.isPresentByUsername(registrationRequest.getUsername())).thenReturn(false);
        when(passwordEncoder.encode(registrationRequest.getPassword())).thenReturn(anyString());

        ResponseEntity<ApiResponse> response = userService.register(registrationRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("User Registered Successfully", Objects.requireNonNull(response.getBody()).getMessage());
        verify(userRepository, times(1)).isPresentByUsername(registrationRequest.getUsername());
        verify(passwordEncoder, times(1)).encode(registrationRequest.getPassword());
    }

    @Test
    void test_cannotSaveUserWithRegisteredUsername_throwsException() {
        when(userRepository.isPresentByUsername(registrationRequest.getUsername())).thenReturn(true);

        assertThrows(UserAlreadyRegistered.class, () -> userService.register(registrationRequest));
        verify(userRepository, times(1)).isPresentByUsername(registrationRequest.getUsername());
        verify(passwordEncoder, never()).encode(registrationRequest.getPassword());
    }

}