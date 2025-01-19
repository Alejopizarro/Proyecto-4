package com.example.user_service.controller.impl;

import com.example.user_service.commons.entities.UserModel;
import com.example.user_service.controller.UserApi;
import com.example.user_service.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController implements UserApi {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public ResponseEntity<UserModel> getUser(String id, Long userId) {
        return ResponseEntity.ok(userService.getUser(id, userId));
    }

    @Override
    public ResponseEntity<UserModel> updateUser(String id, Long userId, UserModel userRequest) {
        return ResponseEntity.ok(userService.updateUser(id, userId, userRequest));
    }

    @Override
    public ResponseEntity<Void> deleteUser(String id, Long userId) {
        userService.deleteUser(id, userId);
        return ResponseEntity.noContent().build();
    }
}
