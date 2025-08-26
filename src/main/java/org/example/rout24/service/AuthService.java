package org.example.rout24.service;

import lombok.RequiredArgsConstructor;
import org.example.rout24.dto.ApiResponse;
import org.example.rout24.dto.request.LoginRequest;
import org.example.rout24.entity.User;
import org.example.rout24.entity.enums.Role;
import org.example.rout24.repository.UserRepository;
import org.example.rout24.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public ApiResponse<String> adminLogin(String username, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtService.generateToken(userDetails);

            return ApiResponse.success(token, "Login successful");
        } catch (Exception e) {
            return ApiResponse.error("Username yoki parol noto‘g‘ri!");
        }
    }

    public ApiResponse<String> userLogin(LoginRequest request,Role role){
        User user = userRepository.findByChatId(request.getChatId())
                .orElseGet(() -> User.builder()
                        .chatId(request.getChatId())
                        .fullName(request.getFullName())
                        .phone(request.getPhone())
                        .region(request.getRegion())
                        .city(request.getCity())
                        .role(role)
                        .telegramAuth(true)
                        .build());

        userRepository.save(user);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getChatId(), "")
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtService.generateToken(userDetails);

        return ApiResponse.success(token, "Login successful");
    }
}
