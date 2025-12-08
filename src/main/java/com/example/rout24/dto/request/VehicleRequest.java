package com.example.rout24.dto.request;

import com.example.rout24.entity.enums.VehicleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Data
public class VehicleRequest {

    @NotBlank(message = "Davlat raqami bo'sh bo'lishi mumkin emas")
    private String plateNumber;
    
    @NotBlank(message = "Mashina nomi bo'sh bo'lishi mumkin emas")
    private String name;
    
    @NotEmpty(message = "Rasmlar bo'sh bo'lishi mumkin emas")
    private List<String> images;
    
    private VehicleType type;
}
