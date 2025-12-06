package com.example.rout24.controller;

import com.example.rout24.dto.ApiResponse;
import com.example.rout24.dto.response.NotificationResponse;
import com.example.rout24.entity.User;
import com.example.rout24.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> getNotifications(
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(notificationService.getNotifications(user));
    }

    @GetMapping("/count")
    public ResponseEntity<ApiResponse<Integer>> getNotificationsCount(
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(notificationService.getNotificationsCount(user));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<String>> clearNotifications(
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(notificationService.clearNotifications(user));
    }
}
