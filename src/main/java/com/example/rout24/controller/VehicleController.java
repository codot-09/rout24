package com.example.rout24.controller;

import com.example.rout24.dto.ApiResponse;
import com.example.rout24.dto.request.VehicleRequest;
import com.example.rout24.dto.response.VehicleResponse;
import com.example.rout24.entity.User;
import com.example.rout24.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<ApiResponse<String>> createVehicle(
            @AuthenticationPrincipal User driver,
            @jakarta.validation.Valid @RequestBody VehicleRequest request
    ){
        return ResponseEntity.ok(vehicleService.saveVehicle(driver, request));
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('DRIVER')")
    public ResponseEntity<ApiResponse<VehicleResponse>> getOwnVehicle(@AuthenticationPrincipal User driver){
        return ResponseEntity.ok(vehicleService.getOwnVehicle(driver));
    }
}
