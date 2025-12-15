package com.deliveryapp.backend.dto.ordersDTO;

import com.deliveryapp.backend.enums.OrderStatus;
import com.deliveryapp.backend.enums.PaymentMethod;
import com.deliveryapp.backend.enums.TransportType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
public class OrderResponseDto {
    private Long id;
    private OrderStatus orderStatus;
    private TransportType transportType;
    private BigDecimal amount;
    private PaymentMethod paymentMethod;
    private Instant createdAt;

    // Координаты
    private double pointALatitude;
    private double pointALongitude;
    private double pointBLatitude;
    private double pointBLongitude;

    // Данные клиента (Вместо целого объекта User)
    private Long clientId;
    private String clientName;
    private String clientPhoneNumber;
}