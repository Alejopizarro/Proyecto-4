package com.example.game_service.service;

import io.jsonwebtoken.Claims;

public interface JwtService {
    Claims getClaims(String token);
    boolean isExpired(String token);
    String extractUserId(String token);
}
