package com.example.rout24.controller;

import com.example.rout24.dto.ApiResponse;
import com.example.rout24.dto.request.UserCredentialsRequest;
import com.example.rout24.dto.response.ProfileResponse;
import com.example.rout24.entity.User;
import com.example.rout24.service.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Foydalanuvchi")
public class UserController {

    private final UserService userService;

    @PostMapping("/credentials")
    public ResponseEntity<ApiResponse<String>> credentials(
            @AuthenticationPrincipal User user,
            @jakarta.validation.Valid @RequestBody UserCredentialsRequest request
    ){
        return ResponseEntity.ok(userService.setCredentials(user, request));
    }

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<ProfileResponse>> profile(@AuthenticationPrincipal User user){
        return ResponseEntity.ok(userService.profile(user));
    }
}
