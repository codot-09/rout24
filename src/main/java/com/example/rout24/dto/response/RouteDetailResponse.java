package com.example.rout24.dto.response;

import com.example.rout24.entity.enums.Regions;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class RouteDetailResponse {

    private Regions from;
    private Regions to;
    private String fromAddress;
    private String toAddress;
    private BigDecimal price;
    private int seatsCount;
    private LocalDateTime departureDate;
    private boolean finished;
    private UUID vehicleId;
    private String vehicleName;
    private List<String> vehicleImages;
    private String driverId;
    private String driverFullName;
    private String driverContact;
}
