package com.deliveryapp.backend.controller;

import com.deliveryapp.backend.dto.RegistrationRequest;
import com.deliveryapp.backend.model.Courier;
import com.deliveryapp.backend.model.User;
import com.deliveryapp.backend.service.CourierService;
import com.deliveryapp.backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth") // Общий URL для аутентификации
public class RegistrationController {

    private final UserService userService;
    private final CourierService courierService;

    public RegistrationController(UserService userService, CourierService courierService) {
        this.userService = userService;
        this.courierService = courierService;
    }

    // --- ЕДИНАЯ ТОЧКА РЕГИСТРАЦИИ ---
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegistrationRequest request) {

        // ВАЛИДАЦИЯ: Проверка общих обязательных полей
        if (request.getName() == null || request.getPhoneNumber() == null || request.getPassword() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Не все обязательные поля заполнены.");
        }

        // 1. АНАЛИЗ ТИПА ПОЛЬЗОВАТЕЛЯ
        String userType = request.getUserType().toUpperCase();

        if ("COURIER".equals(userType)) {
            // ВАЛИДАЦИЯ: Проверка полей, специфичных для курьера
            if (request.getTransportType() == null || request.getTransportNumber() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Для регистрации курьером требуются данные о транспорте.");
            }

            // 2. Регистрация Курьера
            Courier newCourier = courierService.registerCourier(request);
            return new ResponseEntity<>(newCourier, HttpStatus.CREATED);

        } else if ("USER".equals(userType) || userType == null) { // USER - по умолчанию

            // 3. Регистрация Пользователя
            User newUser = userService.registerUser(request);
            return new ResponseEntity<>(newUser, HttpStatus.CREATED);

        } else {
            // Некорректный тип
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Некорректный тип пользователя.");
        }
    }
}