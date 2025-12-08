package com.example.rout24.dto.request;

import com.example.rout24.entity.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;

@Data
public class DriverInfoRequest {

    @NotBlank(message = "Haydovchilik guvohnomasi bo'sh bo'lishi mumkin emas")
    private String driverLicense;
    
    @NotBlank(message = "Pasport raqami bo'sh bo'lishi mumkin emas")
    private String passportId;
    
    @NotNull(message = "Tug'ilgan sana bo'sh bo'lishi mumkin emas")
    private LocalDate birthDate;

    private Gender gender;
}
