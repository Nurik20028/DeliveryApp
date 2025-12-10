package com.deliveryapp.backend.dto;

import lombok.Data;

@Data
public class CourierLocationDTO {
    private Integer courierId;
    private Double latitude;
    private Double longitude;
}
