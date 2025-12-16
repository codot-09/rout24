package com.example.rout24.service;

import com.example.rout24.dto.request.RouteCreateRequest;
import com.example.rout24.dto.response.PagedResponse;
import com.example.rout24.dto.response.RouteDetailResponse;
import com.example.rout24.dto.response.RouteViewResponse;
import com.example.rout24.entity.Route;
import com.example.rout24.entity.User;
import com.example.rout24.entity.Vehicle;
import com.example.rout24.entity.enums.Regions;
import com.example.rout24.exception.DataNotFoundException;
import com.example.rout24.repository.VehicleRepository;
import com.example.rout24.specification.RouteSpecification;
import com.example.rout24.repository.RouteRepository;
import com.example.rout24.dto.ApiResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RouteService {

    private final RouteRepository routeRepository;
    private final VehicleRepository vehicleRepository;

    public ApiResponse<String> createRout(RouteCreateRequest request, User driver) {
        Vehicle vehicle = vehicleRepository.findByDriver(driver)
                .orElseThrow(() -> new DataNotFoundException("Mashina topilmadi"));

        Route route = Route.builder()
                .fromRegion(request.getFrom())
                .toRegion(request.getTo())
                .driver(driver)
                .price(request.getPrice())
                .seatsCount(request.getSeatsCount())
                .fromAddress(request.getFromAddress())
                .toAddress(request.getToAddress())
                .departureDate(request.getDepartureDate())
                .vehicle(vehicle)
                .build();

        routeRepository.save(route);

        return ApiResponse.success(null, "Reys yaratildi");
    }

    public ApiResponse<List<RouteViewResponse>> getOwnRoutes(User driver, Regions from, Regions to) {
        Specification<Route> spec = RouteSpecification.filterByDriver(driver, from, to);
        List<Route> routes = routeRepository.findAll(spec);

        List<RouteViewResponse> content = routes.stream().map(r -> mapToResponse(r)).toList();
        return ApiResponse.success(content);
    }

    public ApiResponse<PagedResponse<RouteViewResponse>> globalSearch(
            Regions from,
            Regions to,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            LocalDate departureDate,
            Pageable pageable
    ) {
        boolean hasFilter = from != null || to != null || minPrice != null || maxPrice != null || departureDate != null;

        Specification<Route> spec = (root, query, cb) -> cb.and(
                cb.isFalse(root.get("finished")),
                cb.greaterThan(root.get("seatsCount"), 0)
        );

        if (hasFilter) {
            spec = spec.and(
                    RouteSpecification.globalFilter(from, to, minPrice, maxPrice, departureDate)
            );
        }

        Page<Route> routePage = routeRepository.findAll(spec, pageable);

        List<RouteViewResponse> content = routePage.getContent()
                .stream()
                .map(this::mapToResponse)
                .toList();

        PagedResponse<RouteViewResponse> pagedResponse = new PagedResponse<>();
        pagedResponse.setContent(content);
        pagedResponse.setPageNumber(routePage.getNumber());
        pagedResponse.setPageSize(routePage.getSize());
        pagedResponse.setTotalElements(routePage.getTotalElements());
        pagedResponse.setTotalPages(routePage.getTotalPages());
        pagedResponse.setLast(routePage.isLast());

        return ApiResponse.success(pagedResponse);
    }

    public ApiResponse<RouteDetailResponse> getRouteById(UUID routeId) {
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new DataNotFoundException("Reys topilmadi"));

        RouteDetailResponse response = new RouteDetailResponse();
        response.setFrom(route.getFromRegion());
        response.setTo(route.getToRegion());
        response.setFromAddress(route.getFromAddress());
        response.setToAddress(route.getToAddress());
        response.setPrice(route.getPrice());
        response.setSeatsCount(route.getSeatsCount());
        response.setDepartureDate(route.getDepartureDate());
        response.setFinished(route.isFinished());

        if (route.getVehicle() != null) {
            response.setVehicleId(route.getVehicle().getId());
            response.setVehicleName(route.getVehicle().getName());
            response.setVehicleImages(route.getVehicle().getImages() != null
                    ? route.getVehicle().getImages()
                    : List.of());
        }

        if (route.getDriver() != null) {
            response.setDriverId(route.getDriver().getChatId());
            response.setDriverFullName(route.getDriver().getFullName());
            response.setDriverContact(route.getDriver().getTgUsername());
        }

        return ApiResponse.success(response);
    }

    public ApiResponse<String> finishRoute(UUID id){
        Route route = routeRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Reys topilmadi"));

        route.setFinished(true);

        routeRepository.save(route);

        return ApiResponse.success(null,"Reys tugatildi");
    }

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void autoFinishRoutes() {
        LocalDate yesterday = LocalDate.now().minusDays(1);

        routeRepository.findAllByFinishedFalseAndDepartureDateBefore(yesterday)
                .forEach(route -> route.setFinished(true));

        routeRepository.flush();
    }

    private RouteViewResponse mapToResponse(Route r) {
        RouteViewResponse response = new RouteViewResponse();
        response.setId(r.getId());
        response.setFrom(r.getFromRegion());
        response.setTo(r.getToRegion());
        response.setPrice(r.getPrice());
        response.setCoverImageUrl(r.getVehicle() != null && r.getVehicle().getImages() != null && !r.getVehicle().getImages().isEmpty()
                ? r.getVehicle().getImages().get(0)
                : null);
        return response;
    }

}
