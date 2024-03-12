package FulfilmentService.exceptions;

import jakarta.persistence.EntityExistsException;

public class DeliveryValetAssignedException extends EntityExistsException {
    public DeliveryValetAssignedException() {
        super();
    }
}
