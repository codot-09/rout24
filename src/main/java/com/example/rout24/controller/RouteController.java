package com.example.rout24.controller;

import com.example.rout24.dto.ApiResponse;
import com.example.rout24.dto.request.RouteCreateRequest;
import com.example.rout24.dto.response.PagedResponse;
import com.example.rout24.dto.response.RouteDetailResponse;
import com.example.rout24.dto.response.RouteViewResponse;
import com.example.rout24.entity.User;
import com.example.rout24.entity.enums.Regions;
import com.example.rout24.service.RouteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/routes")
@RequiredArgsConstructor
@Tag(name = "Reys")
public class RouteController {

    private final RouteService routeService;

    @PostMapping
    public ResponseEntity<ApiResponse<String>> createRoute(
            @AuthenticationPrincipal User driver,
            @RequestBody RouteCreateRequest request
    ){
        return ResponseEntity.ok(routeService.createRout(request, driver));
    }

    @GetMapping("/my-routes")
    public ResponseEntity<ApiResponse<List<RouteViewResponse>>> getOwnRoutes(
            @AuthenticationPrincipal User driver,
            @RequestParam(required = false) Regions from,
            @RequestParam(required = false) Regions to
    ){
        return ResponseEntity.ok(routeService.getOwnRoutes(driver, from,to));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<PagedResponse<RouteViewResponse>>> getRoutes(
            @RequestParam(required = false) Regions from,
            @RequestParam(required = false) Regions to,
            @RequestParam(required = false)BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false)LocalDate departureDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Pageable pageable = PageRequest.of(page,size);
        return ResponseEntity.ok(routeService.globalSearch(
                from,
                 to,
                 minPrice,
                 maxPrice,
                departureDate,pageable)
        );
    }

    @GetMapping("/{routeId}")
    public ResponseEntity<ApiResponse<RouteDetailResponse>> getById(@PathVariable UUID routeId){
        return ResponseEntity.ok(routeService.getRouteById(routeId));
    }
}
