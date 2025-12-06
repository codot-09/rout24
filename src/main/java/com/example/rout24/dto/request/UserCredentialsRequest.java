package com.example.rout24.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserCredentialsRequest {

    @NotBlank(message = "To'liq ism bo'sh bo'lishi mumkin emas")
    private String fullName;
    
    @NotBlank(message = "Rol bo'sh bo'lishi mumkin emas")
    private String role;
    
    private String imageUrl;
}
