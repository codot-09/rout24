package com.example.rout24.controller;

import com.example.rout24.dto.ApiResponse;
import com.example.rout24.dto.request.LoginRequest;
import com.example.rout24.dto.response.LoginResponse;
import com.example.rout24.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @jakarta.validation.Valid @RequestBody LoginRequest request
    ){
        return ResponseEntity.ok(authService.login(request));
    }
}
