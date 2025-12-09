package com.example.rout24.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserStatisticResponse {

    private int routesCount;
    private int ordersCount;
    private BigDecimal monthlyIncome;

    private BigDecimal monthlyExpense;
}
