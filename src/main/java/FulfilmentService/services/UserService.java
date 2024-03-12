package FulfilmentService.services;

import FulfilmentService.dto.ApiResponse;
import FulfilmentService.models.RegistrationRequest;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<ApiResponse> register(RegistrationRequest request);

}
