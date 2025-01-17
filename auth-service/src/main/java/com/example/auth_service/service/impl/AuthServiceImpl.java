package com.example.auth_service.service.impl;

import com.example.auth_service.commons.dtos.LoginRequest;
import com.example.auth_service.commons.dtos.TokenResponse;
import com.example.auth_service.commons.dtos.UserRequest;
import com.example.auth_service.commons.entities.UserModel;
import com.example.auth_service.repository.UserRepository;
import com.example.auth_service.service.AuthService;
import com.example.auth_service.service.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;


    public AuthServiceImpl(UserRepository userRepository, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public TokenResponse createUser(UserRequest userRequest) {
        return Optional.of(userRequest)
                .map(this::mapToEntity)
                .map(userRepository::save)
                .map(userCreated -> jwtService.generateToken(userCreated.getUserId()))
                .orElseThrow(()-> new RuntimeException("Error creating user"));
    }

    @Override
    public TokenResponse loginUser(LoginRequest loginRequest) {
        return Optional.of(loginRequest)
                .map(this::findUserByEmail)
                .filter(user -> validatePassword(loginRequest.getPassword(), user.getPassword()))
                .map((user -> jwtService.generateToken(user.getUserId())))
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private boolean validatePassword(String password, String encodedPassword) {
        if (!passwordEncoder.matches(password, encodedPassword)) {
            throw new RuntimeException("Invalid credentials");
        }
        return true;
    }

    private UserModel findUserByEmail(LoginRequest loginRequest) {
        return userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(()-> new RuntimeException("User not found"));
    }


    private UserModel mapToEntity(UserRequest userRequest) {
        return UserModel.builder()
                .email(userRequest.getEmail())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .name(userRequest.getName())
                .role("USER")
                .build();
    }

}