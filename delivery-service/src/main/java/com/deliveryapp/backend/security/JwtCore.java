package com.deliveryapp.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtCore {

    // Срок действия токена (например, 24 часа в миллисекундах)
    private final long EXPIRATION_TIME = 86400000;

    // Секретный ключ для подписи (должен быть сложным!)
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // 1. Генерация токена
    public String generateToken(String phoneNumber) {
        return Jwts.builder()
                .setSubject(phoneNumber) // Зашиваем номер телефона в токен
                .setIssuedAt(new Date()) // Когда создан
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Когда протухнет
                .signWith(key) // Подписываем нашим секретным ключом
                .compact();
    }

    // 2. Валидация токена и извлечение имени (телефона)
    public String getNameFromJwt(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}