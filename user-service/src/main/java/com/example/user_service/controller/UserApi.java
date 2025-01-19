package com.example.user_service.controller;

import com.example.user_service.commons.constants.ApiPathConstants;
import com.example.user_service.commons.entities.UserModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(ApiPathConstants.V1_ROUTE + ApiPathConstants.USER_ROUTE)
public interface UserApi {
    @GetMapping(value = "/{userId}")
    ResponseEntity<UserModel> getUser(
            @RequestHeader("userIdRequest") String id,
            @PathVariable Long userId
    );

    @PutMapping(value = "/{userId}")
    ResponseEntity<UserModel> updateUser(
            @RequestHeader("userIdRequest") String id,
            @PathVariable Long userId,
            @RequestBody UserModel userRequest
    );

    @DeleteMapping(value = "/{userId}")
    ResponseEntity<Void> deleteUser(
            @RequestHeader("userIdRequest") String id,
            @PathVariable Long userId
    );
}
