package com.example.rout24.dto.request;

import com.example.rout24.entity.enums.Regions;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class RouteCreateRequest {

    private Regions from;
    private Regions to;

    private String fromAddress;

    private String toAddress;

    private int seatsCount;
    private BigDecimal price;

    private LocalDateTime departureDate;
}
