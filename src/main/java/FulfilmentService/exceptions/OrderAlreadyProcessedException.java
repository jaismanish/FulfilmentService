package FulfilmentService.exceptions;

import jakarta.persistence.EntityExistsException;

public class OrderAlreadyProcessedException extends EntityExistsException {
    public OrderAlreadyProcessedException() {
        super();
    }
}
