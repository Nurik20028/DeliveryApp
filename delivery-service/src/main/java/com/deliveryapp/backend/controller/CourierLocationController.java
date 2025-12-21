// здесь мы принимаем данные от курьера
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
        System.out.println("ПРИШЛО НА СЕРВЕР: Курьер " + location.getCourierId() + " "+ location.getLongitude() + "  "+ location.getLatitude());

        messagingTemplate.convertAndSend("/topic/courier/" + location.getCourierId(), location);

        System.out.println("ОТПРАВИЛ В /topic/courier/26");
    }
}