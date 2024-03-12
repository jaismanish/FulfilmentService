package FulfilmentService.models;

import FulfilmentService.dto.Address;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryRequest {
    private long orderId;

    @Valid
    private Address pickupAddress;

    @Valid
    private Address deliveryAddress;

}
