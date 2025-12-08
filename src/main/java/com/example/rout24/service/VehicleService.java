package com.example.rout24.service;

import com.example.rout24.dto.ApiResponse;
import com.example.rout24.dto.request.VehicleRequest;
import com.example.rout24.dto.response.VehicleResponse;
import com.example.rout24.entity.User;
import com.example.rout24.entity.Vehicle;
import com.example.rout24.entity.enums.VehicleType;
import com.example.rout24.exception.DataNotFoundException;
import com.example.rout24.exception.InvalidRequestException;
import com.example.rout24.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final DriverService driverService;

    @Transactional
    public ApiResponse<String> saveVehicle(User driver, VehicleRequest request) {
        if (driver.getRole() == null || driver.getRole() != com.example.rout24.entity.enums.UserRole.DRIVER) {
            throw new InvalidRequestException("Bu foydalanuvchi haydovchi emas");
        }

        if (!driverService.checkAVerified(driver)) {
            throw new InvalidRequestException("Malumotlar tasdiqlanishi kutilmoqda");
        }
        
        if (vehicleRepository.existsByDriver(driver)) {
            throw new InvalidRequestException("Mashina malumotlari allaqachon mavjud");
        }
        
        if (vehicleRepository.existsByPlateNumber(request.getPlateNumber())) {
            throw new InvalidRequestException("Bu davlat raqami allaqachon mavjud");
        }

        VehicleType type;
        try {
            type = request.getType();
        } catch (IllegalArgumentException e) {
            throw new InvalidRequestException("Noto'g'ri mashina turi: " + request.getType());
        }

        Vehicle vehicle = Vehicle.builder()
                .driver(driver)
                .name(request.getName())
                .plateNumber(request.getPlateNumber())
                .images(request.getImages())
                .type(type)
                .build();

        vehicleRepository.save(vehicle);
        log.debug("Vehicle saved for driver: {}", driver.getChatId());

        return ApiResponse.success(null, "Mashina malumotlari qo'shildi");
    }

    @Transactional(readOnly = true)
    public ApiResponse<VehicleResponse> getOwnVehicle(User driver) {
        if (driver.getRole() == null || driver.getRole() != com.example.rout24.entity.enums.UserRole.DRIVER) {
            throw new InvalidRequestException("Bu foydalanuvchi haydovchi emas");
        }

        Vehicle vehicle = vehicleRepository.findByDriver(driver)
                .orElseThrow(() -> new DataNotFoundException("Mashina malumotlari topilmadi"));

        VehicleResponse response = new VehicleResponse();
        response.setPlateNumber(vehicle.getPlateNumber());
        response.setName(vehicle.getName());
        response.setType(vehicle.getType().name());
        response.setImages(vehicle.getImages());

        return ApiResponse.success(response);
    }
}
