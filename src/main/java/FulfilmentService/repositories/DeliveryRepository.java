package FulfilmentService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery, String> {
    boolean isPresentByOrderId(Long id);
}
