package com.deliveryapp.backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class UserLoginResponseDto {
    // Поля, которые клиент должен увидеть
    private Long id; // ID пользователя, необходим для дальнейших запросов
    private String name;
    private String phoneNumber;
    private BigDecimal usersBalance; // Используем имя поля из сущности User
    private String userType; // Чтобы клиент знал, кто вошел

    //если зашел курьер
    private String courierStatus;
    private String transportType;
    private String transportNumber;
}
