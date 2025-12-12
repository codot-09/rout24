package com.example.rout24.controller;

import com.example.rout24.dto.ApiResponse;
import com.example.rout24.dto.request.OrderCreateRequest;
import com.example.rout24.dto.response.OrderResponse;
import com.example.rout24.dto.response.PagedResponse;
import com.example.rout24.entity.User;
import com.example.rout24.entity.enums.OrderStatus;
import com.example.rout24.entity.enums.Regions;
import com.example.rout24.service.OrderService;
import com.example.rout24.service.QRCodeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Tag(name = "Buyurtma")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create")
    public ApiResponse<String> createOrder(@RequestBody OrderCreateRequest request, @AuthenticationPrincipal User client) {
        return orderService.createOrder(client, request);
    }

    @GetMapping("/route/{routeId}")
    public ApiResponse<List<OrderResponse>> getOrdersByRoute(@PathVariable UUID routeId) {
        return orderService.getOrdersByRoute(routeId);
    }

    @GetMapping("/own")
    public ApiResponse<PagedResponse<OrderResponse>> getOwnOrders(
            @AuthenticationPrincipal User client,
            @RequestParam(required = false)Regions from,
            @RequestParam(required = false) Regions to,
            @RequestParam(required = false)OrderStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page,size);
        return orderService.getOwnOrders(client, from,to,status, pageable);
    }

    @PatchMapping("/verify/{param}")
    public ResponseEntity<ApiResponse<String>> verifyOrder(@PathVariable Object param) {
        return ResponseEntity.ok(orderService.verifyOrder(param));
    }

    @PatchMapping("/cancel/{id}")
    public ResponseEntity<ApiResponse<String>> cancelOrder(@PathVariable UUID id) {
        return ResponseEntity.ok(orderService.cancelOrder(id));
    }
}
