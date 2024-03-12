package FulfilmentService.controllers;

import FulfilmentService.dto.ApiResponse;
import FulfilmentService.exceptions.DeliveryNotFoundException;
import FulfilmentService.exceptions.NoDeliveryValetFoundNearbyException;
import FulfilmentService.models.DeliveryRequest;
import FulfilmentService.services.DeliveryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/deliveries")
public class DeliveryController {
    private final DeliveryService deliveryService;

    @PostMapping
    public ResponseEntity<ApiResponse> process(@RequestBody DeliveryRequest request) throws JsonProcessingException, NoDeliveryValetFoundNearbyException {
        return this.deliveryService.process(request);
    }

    @PutMapping("/{deliveryId}")
    public ResponseEntity<ApiResponse> updateStatus(@PathVariable(value = "deliveryId") String deliveryId) throws DeliveryNotFoundException {
        return this.deliveryService.updateStatus(deliveryId);
    }

}