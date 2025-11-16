package com.deliveryapp.backend.service;

import com.deliveryapp.backend.dto.RegistrationRequest;
import com.deliveryapp.backend.model.User;
import com.deliveryapp.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // - от сюда скопировал
    public User registerUser(RegistrationRequest request) {
        // Создаем новую сущность User
        User newUser = new User();

        // 1. Копируем общие поля
        newUser.setName(request.getName());
        newUser.setPhoneNumber(request.getPhoneNumber());
        newUser.setPassword(request.getPassword()); // !!! ВАЖНО: ХЭШИРОВАТЬ !!!

        // 2. Устанавливаем баланс по умолчанию (если нужно)
         newUser.setUsersBalance(BigDecimal.ZERO);

        return userRepository.save(newUser);
    }

    // ... другие методы (save, login и т.д.) - до сюда
}
