package com.deliveryapp.backend.service;

import com.deliveryapp.backend.dto.RegistrationRequest;
import com.deliveryapp.backend.model.Courier;
import com.deliveryapp.backend.enums.TransportType;
import com.deliveryapp.backend.repository.CourierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourierService {
    private final CourierRepository courierRepository;

    // --- 2. ДОБАВЬТЕ ЭТОТ КОНСТРУКТОР ---
    @Autowired
    public CourierService(CourierRepository courierRepository) {
        this.courierRepository = courierRepository;
    }
    public Courier registerCourier(RegistrationRequest request) {

        // Создаем новую сущность Courier
        Courier newCourier = new Courier();

        // 1. Копируем общие поля
        newCourier.setName(request.getName());
        newCourier.setPhoneNumber(request.getPhoneNumber());
        newCourier.setPassword(request.getPassword()); // !!! ВАЖНО: ХЭШИРОВАТЬ !!!

        // 2. Копируем специфичные поля Курьера
        newCourier.setTransportType(request.getTransportType()); // Используйте ваш Enum
        newCourier.setTransportNumber(request.getTransportNumber()); // Исправьте опечатку, если еще не сделали!
        // newCourier.setCourierStatus(CourierStatus.OFFLINE); // Начальный статус

        return courierRepository.save(newCourier);
    }

    // ... другие методы (save, login и т.д.)
}
