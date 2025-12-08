package com.example.rout24.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
public class UserCredentialsRequest {

    @NotBlank(message = "To'liq ism bo'sh bo'lishi mumkin emas")
    private String fullName;
    
    @NotBlank(message = "Rol bo'sh bo'lishi mumkin emas")
    private String role;
    
    private String imageUrl;
}
