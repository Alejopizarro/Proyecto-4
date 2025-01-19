package com.example.user_service.service;

import io.jsonwebtoken.Claims;

public interface JwtService {
    Claims getClaims(String token);
    boolean isExpired(String token);
    String extractUserId(String token);
}
