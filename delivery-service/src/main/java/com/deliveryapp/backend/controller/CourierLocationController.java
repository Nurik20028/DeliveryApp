package com.deliveryapp.backend.controller;

import com.deliveryapp.backend.dto.CourierLocationDTO;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CourierLocationController {

    private final SimpMessagingTemplate messagingTemplate;

    public CourierLocationController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    // Курьер отправляет свою геолокацию
    @MessageMapping("/courier/location")
    public void updateLocation(CourierLocationDTO location) {
        messagingTemplate.convertAndSend("/topic/courier/" + location.getCourierId(), location);
    }
}
