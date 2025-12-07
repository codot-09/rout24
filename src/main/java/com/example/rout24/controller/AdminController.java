package com.example.rout24.controller;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.rout24.dto.ApiResponse;
import com.example.rout24.dto.response.UnverifiedDriverInfoResponse;
import com.example.rout24.entity.enums.RequestStatus;
import com.example.rout24.service.AdminService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Tag(name = "Admin")
public class AdminController {

    private final AdminService adminService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<UnverifiedDriverInfoResponse>>> getUnverifiedRequests(
        @RequestParam(required = false) LocalDate from,
        @RequestParam(required = false) LocalDate to,
        @RequestParam(required = false) String id,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ){
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(adminService.getUnverifiedRequests(from, to, id, pageable));
    }

    @PatchMapping("/{requestId}")
    public ResponseEntity<ApiResponse<String>> confirmRequest(
        @PathVariable UUID requestId,
        @RequestParam RequestStatus status
    ){
        return ResponseEntity.ok(adminService.confirmRequest(requestId, status));
    }

}
