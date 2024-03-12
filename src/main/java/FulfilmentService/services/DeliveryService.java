package FulfilmentService.services;

import FulfilmentService.dto.ApiResponse;
import FulfilmentService.exceptions.DeliveryNotFoundException;
import FulfilmentService.exceptions.NoDeliveryValetFoundNearbyException;
import FulfilmentService.models.DeliveryRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

public interface DeliveryService {
    ResponseEntity<ApiResponse> process(DeliveryRequest request) throws NoDeliveryValetFoundNearbyException, JsonProcessingException;
    ResponseEntity<ApiResponse> updateStatus(String deliveryId) throws DeliveryNotFoundException;
}
