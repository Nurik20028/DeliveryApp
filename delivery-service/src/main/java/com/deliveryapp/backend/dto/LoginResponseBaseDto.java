package com.deliveryapp.backend.dto;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class LoginResponseBaseDto {
    private Long id;
    private String name;
    private String phoneNumber;
    private String userType;

    private String accessToken;
}
