package com.example.rout24.dto.response;

import com.example.rout24.entity.enums.Regions;
import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class RouteViewResponse {

    private UUID id;
    private Regions from;
    private Regions to;
    private BigDecimal price;
    private String coverImageUrl;
    private boolean finished;
}
