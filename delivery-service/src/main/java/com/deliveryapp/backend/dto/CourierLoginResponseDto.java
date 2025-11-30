package com.deliveryapp.backend.dto;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class CourierLoginResponseDto extends LoginResponseBaseDto {
    private String courierStatus; // Используйте корректное имя
    private String transportType;
    private String transportNumber;
    private BigDecimal usersBalance;
}
