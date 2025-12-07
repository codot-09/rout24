package com.example.rout24.service;

import com.example.rout24.dto.ApiResponse;
import com.example.rout24.dto.request.LoginRequest;
import com.example.rout24.dto.response.LoginResponse;
import com.example.rout24.entity.User;
import com.example.rout24.exception.InvalidRequestException;
import com.example.rout24.repository.UserRepository;
import com.example.rout24.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    @Transactional
    public ApiResponse<LoginResponse> login(LoginRequest request) {
        User user = userRepository.findById(request.getChatId())
                .orElseGet(() -> createUser(request));

        if (user.isBlocked()) {
            return ApiResponse.error("Profil bloklangan");
        }

        LocalDate today = LocalDate.now();
        if (user.getLastLogin() == null || !user.getLastLogin().equals(today)) {
            user.setLastLogin(today);
            userRepository.save(user);
        }

        log.debug("User logged in: {}", user.getChatId());

        String token = jwtProvider.generateToken(user.getChatId());

        LoginResponse response = new LoginResponse();
        response.setTokenType("Bearer");
        response.setToken(token);
        response.setRole(user.getRole() != null ? user.getRole().name() : null);

        return ApiResponse.success(response);
    }

    @Transactional
    private User createUser(LoginRequest request) {
        if (request.getUsername() != null && !request.getUsername().isEmpty() 
                && userRepository.existsByTgUsername(request.getUsername())) {
            throw new InvalidRequestException("Bu username allaqachon mavjud");
        }
        
        User newUser = User.builder()
                .chatId(request.getChatId())
                .tgUsername(request.getUsername() != null && !request.getUsername().isEmpty() ? request.getUsername() : null)
                .imageUrl(request.getImageUrl())
                .blocked(false)
                .premiumUser(false)
                .build();
        return userRepository.save(newUser);
    }
}
