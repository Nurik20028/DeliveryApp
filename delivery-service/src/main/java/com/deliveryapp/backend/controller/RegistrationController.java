package com.deliveryapp.backend.controller;

import com.deliveryapp.backend.dto.LoginResponseBaseDto;
import com.deliveryapp.backend.dto.RegistrationRequest;
import com.deliveryapp.backend.dto.UserLoginRequestDto;
import com.deliveryapp.backend.dto.UserLoginResponseDto;
import com.deliveryapp.backend.model.Courier;
import com.deliveryapp.backend.model.User;
import com.deliveryapp.backend.security.JwtCore;
import com.deliveryapp.backend.service.CourierService;
import com.deliveryapp.backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth") // Общий URL для аутентификации
//@CrossOrigin(origins = "*")
public class RegistrationController {

    private final UserService userService;
    private final CourierService courierService;
    private final JwtCore jwtCore;

    public RegistrationController(UserService userService, CourierService courierService, JwtCore jwtCore) {
        this.userService = userService;
        this.courierService = courierService;
        this.jwtCore = jwtCore;
    }

    // --- ЕДИНАЯ ТОЧКА РЕГИСТРАЦИИ ---
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegistrationRequest request) {

        // ВАЛИДАЦИЯ: Проверка общих обязательных полей
        if (request.getName() == null || request.getPhoneNumber() == null || request.getPassword() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Не все обязательные поля заполнены.");
        }

        // 1. АНАЛИЗ ТИПА ПОЛЬЗОВАТЕЛЯ user courier
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

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody RegistrationRequest request) {

        // 1. ПРОВЕРКА ОБЯЗАТЕЛЬНЫХ ПОЛЕЙ
        if (request.getPhoneNumber() == null || request.getPassword() == null || request.getUserType() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Не все обязательные поля (телефон, пароль, тип) заполнены.");
        }

        String userType = request.getUserType().toUpperCase();
        // loggedInUserDto теперь имеет конкретный тип - DTO ответа
        LoginResponseBaseDto loggedInUserDto = null;

        // 2. ПОДГОТОВКА DTO ЗАПРОСА
        UserLoginRequestDto loginRequestDto = new UserLoginRequestDto();
        loginRequestDto.setPhoneNumber(request.getPhoneNumber());
        loginRequestDto.setPassword(request.getPassword());


        // 3. АНАЛИЗ ТИПА И ВЫЗОВ СЕРВИСА
        if ("COURIER".equals(userType)) {
            loggedInUserDto = courierService.loginCourier(loginRequestDto);

        } else if ("USER".equals(userType)) {
            loggedInUserDto = userService.loginUser(loginRequestDto);

        } else {
            // Некорректный тип пользователя
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ошибка: Недопустимый тип пользователя: " + request.getUserType());
        }

        // 4. ЕДИНАЯ ПРОВЕРКА РЕЗУЛЬТАТА
        if (loggedInUserDto != null) {
            // 1. Генерируем токен на основе телефона
            String token = jwtCore.generateToken(loggedInUserDto.getPhoneNumber());

            // 2. Кладем токен в ответ
            loggedInUserDto.setAccessToken(token);
            // Успешный вход: возвращаем DTO и статус 200 OK
            return new ResponseEntity<>(loggedInUserDto, HttpStatus.OK);
        } else {
            // Неудачный вход (неверный логин/пароль)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Ошибка: Неверный номер телефона или пароль.");
        }
    }
}