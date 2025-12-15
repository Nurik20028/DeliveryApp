package com.deliveryapp.backend.service;

import com.deliveryapp.backend.dto.RegistrationRequest;
import com.deliveryapp.backend.dto.UserLoginRequestDto;
import com.deliveryapp.backend.dto.UserLoginResponseDto;
import com.deliveryapp.backend.model.User;
import com.deliveryapp.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // - от сюда скопировал
    public User registerUser(RegistrationRequest request) {
        // Создаем новую сущность User
        User newUser = new User();

        // 1. Копируем общие поля
        newUser.setName(request.getName());
        newUser.setPhoneNumber(request.getPhoneNumber());
        newUser.setPassword(passwordEncoder.encode(request.getPassword())); // !!! ВАЖНО: ХЭШИРОВАТЬ !!!

        // 2. Устанавливаем баланс по умолчанию (если нужно)
        newUser.setUsersBalance(BigDecimal.ZERO);

        return userRepository.save(newUser);
    }

    // Принимает Request DTO, возвращает Response DTO
    public UserLoginResponseDto loginUser(UserLoginRequestDto request) {

        // 1. Поиск пользователя (если не найден, oldUser = null)
        User oldUser = userRepository.findByPhoneNumber(request.getPhoneNumber());

        // 2. Проверка, найден ли пользователь
        if (oldUser == null) {
            return null; // Пользователь не найден
        }

        // 3. Проверка пароля (Используем .equals() для сравнения строк!)
        // ВАЖНО: В реальном проекте здесь должно быть сравнение хешей!
        // 1-й аргумент: пароль, который ввел пользователь (request.getPassword())
        // 2-й аргумент: хеш, который лежит в БД (oldUser.getPassword())
        if (passwordEncoder.matches(request.getPassword(), oldUser.getPassword())) {

            // 4. Пароли совпали: Преобразуем Entity в Response DTO
            return convertToDto(oldUser);
        }

        // Если пароли не совпали
        return null;
    }


    // --- ПРИВАТНЫЙ МЕТОД ПРЕОБРАЗОВАНИЯ ---
    private UserLoginResponseDto convertToDto(User user) {
        UserLoginResponseDto dto = new UserLoginResponseDto();

        // Копируем данные из Entity (User) в DTO
        dto.setId(Long.valueOf(user.getId()));
        dto.setName(user.getName());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setUsersBalance(user.getUsersBalance()); // Используем корректный геттер
        dto.setUserType("USER"); // Явно указываем тип

        return dto;
    }
    // ... другие методы (save, login и т.д.) - до сюда
}
