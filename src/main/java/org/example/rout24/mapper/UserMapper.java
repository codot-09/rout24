package org.example.rout24.mapper;

import org.example.rout24.dto.response.UserResponse;
import org.example.rout24.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserResponse toResponse(User user){
        return new UserResponse(
                user.getFullName(),
                user.getPhone(),
                user.getRegion(),
                user.getCity(),
                user.getImageUrl()
        );
    }
}
