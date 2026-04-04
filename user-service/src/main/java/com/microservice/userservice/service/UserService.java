package com.microservice.userservice.service;

import com.microservice.userservice.exception.ResourceNotFoundException;
import com.microservice.userservice.model.User;
import com.microservice.userservice.model.UserDTO;
import com.microservice.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserDTO.UserResponse createUser(UserDTO.CreateUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already in use: " + request.getEmail());
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());

        User saved = userRepository.save(user);
        return UserDTO.UserResponse.fromUser(saved);
    }

    public List<UserDTO.UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserDTO.UserResponse::fromUser)
                .collect(Collectors.toList());
    }

    public UserDTO.UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return UserDTO.UserResponse.fromUser(user);
    }
}
