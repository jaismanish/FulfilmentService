package FulfilmentService.services;

import FulfilmentService.dto.ApiResponse;
import FulfilmentService.models.UserRequest;
import FulfilmentService.models.UserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;

import java.util.Map;

public class UserServiceImpl implements UserService{
    @Override
    public UserResponse register(UserRequest user) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException();
        }

        User user = User.builder()
                .name(request.getName())
                .address(request.getAddress())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        userRepository.save(user);

        ApiResponse response = ApiResponse.builder()
                .message("User Registered Successfully")
                .status(HttpStatus.CREATED)
                .data(Map.of("user", new UserResponse(user)))
                .build();

        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
