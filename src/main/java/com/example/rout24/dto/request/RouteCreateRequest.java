package com.example.rout24.dto.request;

import com.example.rout24.entity.enums.Regions;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class RouteCreateRequest {

    private Regions from;
    private Regions to;

    private Double fromLat;
    private Double fromLng;
    private String fromAddress;

    private Double toLat;
    private Double toLng;
    private String toAddress;

    private int seatsCount;
    private BigDecimal price;

    private LocalDateTime departureDate;
}
