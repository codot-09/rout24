package com.example.rout24.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
public class LoginRequest {

    @NotBlank(message = "Chat ID bo'sh bo'lishi mumkin emas")
    private String chatId;
    
    private String username;
    
    private String imageUrl;
}
