package com.example.rout24.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VehicleRequest {

    @NotBlank(message = "Davlat raqami bo'sh bo'lishi mumkin emas")
    private String plateNumber;
    
    @NotBlank(message = "Mashina nomi bo'sh bo'lishi mumkin emas")
    private String name;
    
    @NotEmpty(message = "Rasmlar bo'sh bo'lishi mumkin emas")
    private List<String> images;
    
    @NotBlank(message = "Mashina turi bo'sh bo'lishi mumkin emas")
    private String type;
}
