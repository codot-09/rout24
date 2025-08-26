package org.example.rout24.service;

import lombok.RequiredArgsConstructor;
import org.example.rout24.dto.ApiResponse;
import org.example.rout24.dto.response.UserResponse;
import org.example.rout24.entity.User;
import org.example.rout24.mapper.UserMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper mapper;

    public ApiResponse<UserResponse> getProfile(User user){
        return ApiResponse.success(mapper.toResponse(user));
    }
}
