package FulfilmentService.dto;

import FulfilmentService.entities.Delivery;
import FulfilmentService.enums.DeliveryStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DeliveryResponse {
    private String id;
    private long orderId;
    private String executiveId;
    private DeliveryStatus status;

    public DeliveryResponse(Delivery delivery) {
        this.id = delivery.getId();
        this.orderId = delivery.getOrderId();
        this.executiveId = delivery.getUser().getId();
        this.status = delivery.getStatus();
    }
}