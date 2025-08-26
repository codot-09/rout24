package org.example.rout24.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.example.rout24.dto.ApiResponse;
import org.example.rout24.dto.request.LoginRequest;
import org.example.rout24.entity.enums.Role;
import org.example.rout24.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/user/login")
    @Operation(summary = "User uchun login")
    public ResponseEntity<ApiResponse<String>> userLogin(
            @RequestBody LoginRequest request,
            @RequestParam Role role
    ){
        return ResponseEntity.ok(authService.userLogin(request, role));
    }

    @PostMapping("/admin/login")
    @Operation(summary = "Admin uchun login")
    public ResponseEntity<ApiResponse<String>> adminLogin(
            @RequestParam String phone,
            @RequestParam String password
    ){
        return ResponseEntity.ok(authService.adminLogin(phone,password));
    }
}
