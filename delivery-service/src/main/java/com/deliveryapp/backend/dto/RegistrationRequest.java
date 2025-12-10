package com.deliveryapp.backend.dto;

import com.deliveryapp.backend.enums.TransportType;
import lombok.Getter;
import lombok.Setter;

// DTO для регистрации: содержит ВСЕ поля для обоих типов пользователей
@Getter
@Setter
public class RegistrationRequest {

    // Общие поля для User и Courier
    private String name;
    private String phoneNumber;
    private String password;

    // Поле, определяющее тип регистрации: "USER" или "COURIER"
    private String userType;

    // Дополнительные поля только для Courier
    private TransportType transportType; // Используем ваш Enum
    private String transportNumber;
}
