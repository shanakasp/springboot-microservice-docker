package com.microservice.userservice.controller;

import com.microservice.userservice.model.UserDTO;
import com.microservice.userservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // POST /api/users — Create a new user
    @PostMapping
    public ResponseEntity<UserDTO.UserResponse> createUser(
            @Valid @RequestBody UserDTO.CreateUserRequest request) {
        UserDTO.UserResponse response = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // GET /api/users — Get all users
    @GetMapping
    public ResponseEntity<List<UserDTO.UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // GET /api/users/{id} — Get user by ID (used internally by blog-service)
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO.UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }
}
