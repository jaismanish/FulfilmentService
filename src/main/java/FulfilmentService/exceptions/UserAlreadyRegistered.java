package FulfilmentService.exceptions;

import jakarta.persistence.EntityExistsException;

public class UserAlreadyRegistered extends EntityExistsException {
    public UserAlreadyRegistered() {
        super();
    }
}
