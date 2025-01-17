package com.example.auth_service.service;

import com.example.auth_service.commons.dtos.LoginRequest;
import com.example.auth_service.commons.dtos.TokenResponse;
import com.example.auth_service.commons.dtos.UserRequest;

public interface AuthService {
    TokenResponse createUser(UserRequest userRequest);
    TokenResponse loginUser(LoginRequest loginRequest);
}
