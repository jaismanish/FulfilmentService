package FulfilmentService.services;

import FulfilmentService.dto.ApiResponse;
import FulfilmentService.entities.User;
import FulfilmentService.models.DeliveryRequest;
import FulfilmentService.repositories.DeliveryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

public class DeliveryServiceImpl implements DeliveryService{
    @Autowired
    private DeliveryRepository deliveryRepository;

    @Override
    public ResponseEntity<ApiResponse> process(DeliveryRequest request) {
        if (deliveryRepository.isPresentByOrderId(request.getOrderId())) {
            throw new OrderHasAlreadyBeenFacilitatedException();
        }

        User nearestExecutive = this.getNearestAvailableExecutive(request.getPickupAddress());

        if (nearestExecutive == null) {
            throw new NoExecutiveNearbyException();
        }

        nearestExecutive.setAvailability(Availability.UNAVAILABLE);
        Delivery delivery = Delivery.builder()
                .user(nearestExecutive)
                .restaurantAddress(request.getPickupAddress())
                .customerAddress(request.getDeliveryAddress())
                .orderId(request.getOrderId())
                .build();

        deliveryRepository.save(delivery);
        userRepository.save(nearestExecutive);

        ApiResponse response = ApiResponse.builder()
                .message(DELIVERY_FACILITATED)
                .status(HttpStatus.CREATED)
                .data(Map.of("delivery", delivery))
                .build();

        return ResponseEntity.status(response.getStatus()).body(response);

    }
}
