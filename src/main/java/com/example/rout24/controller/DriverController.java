package com.example.rout24.controller;

import com.example.rout24.dto.ApiResponse;
import com.example.rout24.dto.request.DriverInfoRequest;
import com.example.rout24.dto.response.DriverProfileResponse;
import com.example.rout24.entity.User;
import com.example.rout24.service.DriverService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/drivers")
@RequiredArgsConstructor
@Tag(name = "Haydovchi")
public class DriverController {

    private final DriverService driverService;

    @PutMapping("/finish-account")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<ApiResponse<String>> finishProfile(
            @AuthenticationPrincipal User driver,
            @jakarta.validation.Valid @RequestBody DriverInfoRequest request
    ){
        return ResponseEntity.ok(driverService.finishProfile(driver, request));
    }

    @GetMapping("/profile")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<ApiResponse<DriverProfileResponse>> profile(@AuthenticationPrincipal User driver){
        return ResponseEntity.ok(driverService.profile(driver));
    }
}
