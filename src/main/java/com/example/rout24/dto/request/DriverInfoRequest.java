package com.example.rout24.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DriverInfoRequest {

    @NotBlank(message = "Haydovchilik guvohnomasi bo'sh bo'lishi mumkin emas")
    private String driverLicense;
    
    @NotBlank(message = "Pasport raqami bo'sh bo'lishi mumkin emas")
    private String passportId;
    
    @NotNull(message = "Tug'ilgan sana bo'sh bo'lishi mumkin emas")
    private LocalDate birthDate;
    
    @NotBlank(message = "Jins bo'sh bo'lishi mumkin emas")
    private String gender;
}
