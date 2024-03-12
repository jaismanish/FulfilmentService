package FulfilmentService.services;

import FulfilmentService.models.UserRequest;
import FulfilmentService.models.UserResponse;

public interface UserService {
    UserResponse register(UserRequest user);

}
