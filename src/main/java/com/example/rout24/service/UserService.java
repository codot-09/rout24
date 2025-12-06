package com.example.rout24.service;

import com.example.rout24.dto.ApiResponse;
import com.example.rout24.dto.request.UserCredentialsRequest;
import com.example.rout24.dto.response.ProfileResponse;
import com.example.rout24.entity.User;
import com.example.rout24.entity.enums.UserRole;
import com.example.rout24.exception.InvalidRequestException;
import com.example.rout24.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    @Lazy
    private final DriverService driverService;

    @Transactional
    public ApiResponse<String> setCredentials(User user, UserCredentialsRequest request) {
        if (user.getRole() != null && user.getRole() == UserRole.DRIVER && 
            !request.getRole().equalsIgnoreCase("DRIVER")) {
            throw new InvalidRequestException("Haydovchi roli o'zgartirilmaydi");
        }

        UserRole role;
        try {
            role = UserRole.valueOf(request.getRole().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidRequestException("Noto'g'ri rol: " + request.getRole());
        }

        user.setImageUrl(request.getImageUrl());
        user.setFullName(request.getFullName());
        
        if (user.getRole() == null) {
            user.setRole(role);
        }

        userRepository.save(user);

        log.debug("User credentials updated: {}", user.getChatId());

        if (role == UserRole.DRIVER && user.getRole() == UserRole.DRIVER) {
            driverService.createInfo(user);
            return ApiResponse.success(null, "Faoliyatni boshlash uchun profilni to'liq yakunlang!");
        }

        return ApiResponse.success(null, "Xush kelibsiz");
    }

    @Transactional(readOnly = true)
    public ApiResponse<ProfileResponse> profile(User user) {
        if (user.getRole() == null) {
            throw new InvalidRequestException("Profil to'liq emas");
        }

        ProfileResponse response = new ProfileResponse();
        response.setFullName(user.getFullName());
        response.setImageUrl(user.getImageUrl());
        
        return ApiResponse.success(response);
    }
}
