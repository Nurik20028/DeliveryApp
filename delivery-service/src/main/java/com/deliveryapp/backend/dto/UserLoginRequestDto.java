package com.deliveryapp.backend.dto;
//DTO будет содержать только данные, которые клиент отправляет для входа.
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginRequestDto {
    private String phoneNumber;
    private String password;
    private String userType;
}
