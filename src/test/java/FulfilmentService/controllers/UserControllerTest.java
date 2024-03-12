package FulfilmentService.controllers;

import FulfilmentService.dto.Address;
import FulfilmentService.exceptions.UserAlreadyRegistered;
import FulfilmentService.models.RegistrationRequest;
import FulfilmentService.services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    Address address = new Address();
    RegistrationRequest registrationRequest = new RegistrationRequest();
    String request;

    @BeforeEach
    void setup() throws JsonProcessingException {

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
        request = objectMapper.writeValueAsString(registrationRequest);

    }

    @Test
    void testCalc(){
        assertEquals(1,1);
    }


    @Test
    void testRegisterUser() throws Exception {
        when(userService.register(registrationRequest)).thenReturn(new ResponseEntity<>(HttpStatus.CREATED));
        mvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        ).andExpect(status().isCreated());
        verify(userService, times(1)).register(registrationRequest);
    }

    @Test
    void testRegisterUserAlreadyRegistered_ShouldThrowUserAlreadyRegistered() throws Exception {
        when(userService.register(registrationRequest)).thenThrow(new UserAlreadyRegistered());
        mvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        ).andExpect(status().isBadRequest());
        verify(userService, times(1)).register(registrationRequest);
    }

}