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
    private final UserEventPublisher userEventPublisher;

    public UserDTO.UserResponse createUser(UserDTO.CreateUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already in use: " + request.getEmail());
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());

        User saved = userRepository.save(user);
        userEventPublisher.publishCreated(saved);
        return UserDTO.UserResponse.fromUser(saved);
    }

    public UserDTO.UserResponse updateUser(Long id, UserDTO.UpdateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        userRepository.findByEmail(request.getEmail())
                .filter(existingUser -> !existingUser.getId().equals(id))
                .ifPresent(existingUser -> {
                    throw new RuntimeException("Email already in use: " + request.getEmail());
                });

        user.setName(request.getName());
        user.setEmail(request.getEmail());

        User saved = userRepository.save(user);
        userEventPublisher.publishUpdated(saved);
        return UserDTO.UserResponse.fromUser(saved);
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        userRepository.delete(user);
        userEventPublisher.publishDeleted(user);
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
