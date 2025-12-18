package com.example.rout24.service;

import com.example.rout24.dto.ApiResponse;
import com.example.rout24.dto.request.DriverInfoRequest;
import com.example.rout24.dto.response.DriverProfileResponse;
import com.example.rout24.entity.DriverInfo;
import com.example.rout24.entity.User;
import com.example.rout24.entity.Vehicle;
import com.example.rout24.entity.enums.Gender;
import com.example.rout24.entity.enums.RequestStatus;
import com.example.rout24.exception.DataNotFoundException;
import com.example.rout24.exception.InvalidRequestException;
import com.example.rout24.repository.DriverRepository;
import com.example.rout24.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DriverService {

    private final DriverRepository driverRepository;
    private final VehicleRepository vehicleRepository;

    @Transactional
    public void createInfo(User driver) {
        if (driverRepository.findByDriverChatId(driver.getChatId()).isPresent()) {
            log.warn("Driver info already exists for: {}", driver.getChatId());
            return;
        }
        DriverInfo info = DriverInfo.builder()
                .driver(driver)
                .status(RequestStatus.NOT_CONFIRMED)
                .build();
        driverRepository.save(info);
        log.debug("Driver info created for: {}", driver.getChatId());
    }

    @Transactional
    public ApiResponse<String> finishProfile(User driver, DriverInfoRequest request) {
        if (driver.getRole() == null || driver.getRole() != com.example.rout24.entity.enums.UserRole.DRIVER) {
            throw new InvalidRequestException("Bu foydalanuvchi haydovchi emas");
        }

        DriverInfo info = driverRepository.findByDriverChatId(driver.getChatId())
                .orElseThrow(() -> new DataNotFoundException("Malumotlar topilmadi"));

        if (info.getStatus().equals(RequestStatus.CONFIRMED)) {
            throw new InvalidRequestException("Profil allaqachon tasdiqlangan");
        }

        Gender gender;
        try {
            gender = request.getGender();
        } catch (IllegalArgumentException e) {
            throw new InvalidRequestException("Noto'g'ri jins: " + request.getGender());
        }

        info.setDriverLicense(request.getDriverLicense());
        info.setPassportId(request.getPassportId());
        info.setGender(gender);
        info.setBirthDate(request.getBirthDate());
        info.setStatus(RequestStatus.PENDING);
        info.setPhoneNumber(request.getPhone());

        driverRepository.save(info);
        log.debug("Driver profile finished for: {}", driver.getChatId());

        return ApiResponse.success(null, "Malumotlar tekshirish uchun yuborildi");
    }

    @Transactional(readOnly = true)
    public ApiResponse<DriverProfileResponse> profile(User driver) {
        if (driver.getRole() == null || driver.getRole() != com.example.rout24.entity.enums.UserRole.DRIVER) {
            throw new InvalidRequestException("Bu foydalanuvchi haydovchi emas");
        }

        DriverInfo info = driverRepository.findByDriverChatId(driver.getChatId())
                .orElseThrow(() -> new DataNotFoundException("Malumotlar topilmadi"));

        Vehicle vehicle = vehicleRepository.findByDriver(driver).orElse(null);

        DriverProfileResponse response = new DriverProfileResponse();
        response.setFullName(driver.getFullName());
        response.setImageUrl(driver.getImageUrl());
        response.setPremiumUser(driver.isPremiumUser());
        response.setStatus(info.getStatus().name());
        
        if (vehicle != null) {
            response.setPlateNumber(vehicle.getPlateNumber());
            if (vehicle.getImages() != null && !vehicle.getImages().isEmpty()) {
                response.setCoverImage(vehicle.getImages().get(0));
            }
        }

        return ApiResponse.success(response);
    }

   @Transactional(readOnly = true)
    public boolean checkAVerified(User driver) {
        return driverRepository.findByDriverChatId(driver.getChatId())
                .map(driverInfo -> driverInfo.getStatus().equals(RequestStatus.CONFIRMED))
                .orElse(false);
    }
}
