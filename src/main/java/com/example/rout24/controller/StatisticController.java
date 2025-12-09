package com.example.rout24.controller;

import com.example.rout24.dto.ApiResponse;
import com.example.rout24.dto.response.UserStatisticResponse;
import com.example.rout24.entity.User;
import com.example.rout24.service.StatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
public class StatisticController {

    private final StatisticService statisticService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserStatisticResponse>> getMyStatistics(@AuthenticationPrincipal User user) {
        ApiResponse<UserStatisticResponse> response = statisticService.getUserStatistics(user);
        return ResponseEntity.ok(response);
    }
}
