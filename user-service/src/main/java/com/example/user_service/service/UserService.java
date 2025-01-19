package com.example.user_service.service;

import com.example.user_service.commons.entities.UserModel;

public interface UserService {
    UserModel getUser(String id, Long userId);
    UserModel updateUser(String id, Long userId, UserModel userRequest);
    void deleteUser(String id, Long userId);
}
