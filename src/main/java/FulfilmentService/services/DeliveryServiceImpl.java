package FulfilmentService.services;

import FulfilmentService.dto.Address;
import FulfilmentService.dto.ApiResponse;
import FulfilmentService.dto.DeliveryResponse;
import FulfilmentService.entities.Delivery;
import FulfilmentService.entities.User;
import FulfilmentService.enums.DeliveryStatus;
import FulfilmentService.enums.DeliveryValetAvailability;
import FulfilmentService.exceptions.*;
import FulfilmentService.models.DeliveryRequest;
import FulfilmentService.repositories.DeliveryRepository;
import FulfilmentService.repositories.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService{

    @Autowired
    private DeliveryRepository deliveryRepository;
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final RestTemplate restTemplate;

    @Override
    public ResponseEntity<ApiResponse> process(DeliveryRequest request) throws NoDeliveryValetFoundNearbyException, JsonProcessingException {
        if (deliveryRepository.isPresentByOrderId(request.getOrderId())) {
            throw new OrderAlreadyProcessedException();
        }

        User nearestExecutive = this.getNearestAvailableExecutive(request.getPickupAddress());

        if (nearestExecutive == null) {
            throw new NoDeliveryValetFoundNearbyException();
        }

        nearestExecutive.setAvailability(DeliveryValetAvailability.UNAVAILABLE);
        Delivery delivery = Delivery.builder()
                .user(nearestExecutive)
                .pickupAddress(request.getPickupAddress())
                .deliveryAddress(request.getDeliveryAddress())
                .orderId(request.getOrderId())
                .build();

        deliveryRepository.save(delivery);
        userRepository.save(nearestExecutive);

        ApiResponse response = ApiResponse.builder()
                .message("Delivery Processed")
                .status(HttpStatus.CREATED)
                .data(Map.of("delivery", delivery))
                .build();

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @Override
    public ResponseEntity<ApiResponse> updateStatus(String deliveryId) throws DeliveryNotFoundException {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(DeliveryNotFoundException::new);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);

        isRespectiveExecutive(delivery, user);

        DeliveryStatus status = delivery.getStatus();

        isAlreadyDelivered(status);

        if (status.equals(DeliveryStatus.PICKED_UP)) {
            delivery.setStatus(DeliveryStatus.DELIVERED);
            user.setAddress(delivery.getDeliveryAddress());
            user.setAvailability(DeliveryValetAvailability.AVAILABLE);
        } else {
            delivery.setStatus(DeliveryStatus.PICKED_UP);
            user.setAddress(delivery.getPickupAddress());
        }
        deliveryRepository.save(delivery);
        userRepository.save(user);

        ApiResponse response = ApiResponse.builder()
                .message("Status Updated" + delivery.getStatus().toString())
                .status(HttpStatus.OK)
                .data(Map.of("delivery", new DeliveryResponse(delivery)))
                .build();

        return ResponseEntity.status(response.getStatus()).body(response);
    }

    private User getNearestAvailableExecutive(Address address) throws JsonProcessingException {
        User user = null;
        List<User> executives = userRepository.findAll();
        ObjectMapper objectMapper = new ObjectMapper();

        String restaurantAddressResponse = this.getDetailedAddress(address);
        JsonNode restaurantAddressJson = objectMapper.readTree(restaurantAddressResponse);
        double restaurantLatitude = restaurantAddressJson.get("lat").asDouble();
        double restaurantLongitude = restaurantAddressJson.get("lon").asDouble();

        double minDistance = Double.MAX_VALUE;
        for (User executive : executives) {
            if (!executive.getAddress().getCity().equals(address.getCity()) || executive.getAvailability().equals(DeliveryValetAvailability.UNAVAILABLE)) {
                continue;
            }

            String executiveAddressResponse = this.getDetailedAddress(executive.getAddress());
            JsonNode executiveAddressJson = objectMapper.readTree(executiveAddressResponse);
            double executiveLatitude = executiveAddressJson.get("lat").asDouble();
            double executiveLongitude = executiveAddressJson.get("lon").asDouble();

            double distance = this.calculateDistance(
                    restaurantLatitude,
                    restaurantLongitude,
                    executiveLatitude,
                    executiveLongitude
            );

            if (distance < minDistance) {
                minDistance = distance;
                user = executive;
            }
        }

        return user;
    }
    private String getDetailedAddress(Address address) {
        String jsonResponse =  String.valueOf(restTemplate.getForEntity(
                "https://nominatim.openstreetmap.org/search?country=India"
                        + "&postalcode=" + address.getPinCode() + "&format=json",
                String.class
        ));
        int startIndex = jsonResponse.indexOf("[");
        int endIndex = jsonResponse.lastIndexOf("}]");

        return jsonResponse.substring(startIndex + 1, endIndex + 1);
    }

    private double calculateDistance(double latitude1, double longitude1, double latitude2, double longitude2) {
        double phi1 = Math.toRadians(latitude1);
        double phi2 = Math.toRadians(latitude2);
        double deltaPhi = Math.toRadians(Math.abs(latitude2 - latitude1));
        double deltaLambda = Math.toRadians(Math.abs(longitude2 - longitude1));
        final double EARTH_RADIUS = 6371.0;

        double a = Math.pow(Math.sin(deltaPhi / 2.0), 2)
                + Math.cos(phi1) * Math.cos(phi2)
                * Math.pow(Math.sin(deltaLambda / 2.0), 2);
        double c = Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }

    private static void isRespectiveExecutive(Delivery delivery, User user) {
        if (!delivery.getUser().equals(user)) {
            throw new UnauthorizedStatusUpdateException();
        }
    }

    private static void isAlreadyDelivered(DeliveryStatus status) {
        if (status.equals(DeliveryStatus.DELIVERED)) {
            throw new OrderAlreadyDeliveredException();
        }
    }

}
