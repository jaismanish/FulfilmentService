package FulfilmentService.services;

import FulfilmentService.dto.ApiResponse;
import FulfilmentService.models.DeliveryRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface DeliveryService {
    ResponseEntity<ApiResponse> process(DeliveryRequest request);
}
