package FulfilmentService.entities;

import FulfilmentService.dto.Address;
import FulfilmentService.enums.DeliveryStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "deliveries")
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private long orderId;

    @ManyToOne
    @JoinColumn(name = "executive_id")
    private User user;

    @AttributeOverrides(value = {
            @AttributeOverride(name = "building", column = @Column(name = "pickup_building")),
            @AttributeOverride(name = "floor", column = @Column(name = "pickup_floor")),
            @AttributeOverride(name = "locality", column = @Column(name = "pickup_locality")),
            @AttributeOverride(name = "city", column = @Column(name = "pickup_city")),
            @AttributeOverride(name = "state", column = @Column(name = "pickup_state")),
            @AttributeOverride(name = "country", column = @Column(name = "pickup_country")),
            @AttributeOverride(name = "pinCode", column = @Column(name = "pickup_pinCode")),})
    private Address pickupAddress;

    @AttributeOverrides(value = {
            @AttributeOverride(name = "building", column = @Column(name = "pickup_building")),
            @AttributeOverride(name = "floor", column = @Column(name = "pickup_floor")),
            @AttributeOverride(name = "locality", column = @Column(name = "pickup_locality")),
            @AttributeOverride(name = "city", column = @Column(name = "pickup_city")),
            @AttributeOverride(name = "state", column = @Column(name = "pickup_state")),
            @AttributeOverride(name = "country", column = @Column(name = "pickup_country")),
            @AttributeOverride(name = "pinCode", column = @Column(name = "pickup_pinCode")),})
    private Address deliveryAddress;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private DeliveryStatus status = DeliveryStatus.WAITING_FOR_PICKUP;

}
