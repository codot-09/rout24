package com.example.rout24.dto.request;

import com.example.rout24.entity.enums.PaymentType;
import lombok.Data;
import java.util.UUID;

@Data
public class OrderCreateRequest {

    private UUID routId;
    private int seatsCount;
    private PaymentType paymentType;
}
