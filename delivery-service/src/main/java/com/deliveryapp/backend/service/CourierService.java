package com.deliveryapp.backend.service;

import com.deliveryapp.backend.dto.CourierLoginResponseDto;
import com.deliveryapp.backend.dto.RegistrationRequest;
import com.deliveryapp.backend.dto.UserLoginRequestDto;
import com.deliveryapp.backend.dto.UserLoginResponseDto;
import com.deliveryapp.backend.enums.CourierStatus;
import com.deliveryapp.backend.model.Courier;
import com.deliveryapp.backend.enums.TransportType;
import com.deliveryapp.backend.repository.CourierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.deliveryapp.backend.enums.CourierStatus.OFFLINE;

@Service
public class CourierService {
    private final CourierRepository courierRepository;
    private final PasswordEncoder passwordEncoder;

    // --- 2. ДОБАВЬТЕ ЭТОТ КОНСТРУКТОР ---
    @Autowired
    public CourierService(CourierRepository courierRepository, PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.courierRepository = courierRepository;
    }
    public Courier registerCourier(RegistrationRequest request) {

        // Создаем новую сущность Courier
        Courier newCourier = new Courier();

        // 1. Копируем общие поля
        newCourier.setName(request.getName());
        newCourier.setPhoneNumber(request.getPhoneNumber());
        newCourier.setPassword(passwordEncoder.encode(request.getPassword())); // !!! ВАЖНО: ХЭШИРОВАТЬ !!!

        // 2. Копируем специфичные поля Курьера
        newCourier.setTransportType(request.getTransportType()); // Используйте ваш Enum
        newCourier.setTransportNumber(request.getTransportNumber()); // Исправьте опечатку, если еще не сделали!
        newCourier.setCourierStatus(CourierStatus.OFFLINE); // Начальный статус

        return courierRepository.save(newCourier);
    }

    public CourierLoginResponseDto loginCourier(UserLoginRequestDto request) {

        Courier oldCourier = courierRepository.findByPhoneNumber(request.getPhoneNumber());

        if (oldCourier == null) {
            return null;
        }

        // 2. Проверка пароля
        if (passwordEncoder.matches(request.getPassword(), oldCourier.getPassword())) {

            // 3. Пароли совпали: Преобразуем Entity в Response DTO
            return convertCourierToDto(oldCourier);
        }

        return null;
    }

    // --- ПРИВАТНЫЙ МЕТОД ПРЕОБРАЗОВАНИЯ ДЛЯ КУРЬЕРА ---
    private CourierLoginResponseDto convertCourierToDto(Courier courier) {
        CourierLoginResponseDto dto = new CourierLoginResponseDto();

        // Копируем общие поля Курьера в общий DTO ответа
        dto.setId(Long.valueOf(courier.getId()));
        dto.setName(courier.getName());
        dto.setPhoneNumber(courier.getPhoneNumber());
        dto.setUserType("COURIER");// Явно указываем тип

        dto.setCourierStatus(String.valueOf(CourierStatus.ONLINE));
        dto.setTransportType(courier.getTransportType().toString());
        dto.setTransportNumber(courier.getTransportNumber());

        // Баланс курьера (если есть) или можно оставить null/0
         dto.setUsersBalance(courier.getCourierBalance());

        return dto;
    }

    // ... другие методы (save, login и т.д.)
}
