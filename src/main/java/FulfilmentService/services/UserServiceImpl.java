package FulfilmentService.services;

import FulfilmentService.dto.ApiResponse;
import FulfilmentService.dto.UserResponse;
import FulfilmentService.entities.User;
import FulfilmentService.exceptions.UserAlreadyRegistered;
import FulfilmentService.models.RegistrationRequest;
import FulfilmentService.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;

public class UserServiceImpl implements UserService{
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ResponseEntity<ApiResponse> register(RegistrationRequest request) {
        if (userRepository.isPresentByUsername(request.getUsername())) {
            throw new UserAlreadyRegistered();
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
