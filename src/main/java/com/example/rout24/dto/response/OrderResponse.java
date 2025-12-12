package com.example.rout24.dto.response;

import com.example.rout24.entity.enums.OrderStatus;
import com.example.rout24.entity.enums.PaymentStatus;
import com.example.rout24.entity.enums.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {

    private UUID id;
    private Integer billingNumber;
    private String qrCode;
    private LocalDateTime orderDate;
    private PaymentStatus paymentStatus;
    private PaymentType paymentType;
    private UUID routeId;
    private BigDecimal price;
    private int seatsCount;
    private String clientName;
    private OrderStatus status;
}
