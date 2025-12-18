package com.example.rout24.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AdminStatisticResponse {

    private long usersCount;
    private long driversCount;
    private BigDecimal moneyCirculation;
}
