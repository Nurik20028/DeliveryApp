package com.deliveryapp.backend.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtCore {

    // Срок действия токена (24 часа в миллисекундах)
    private final long EXPIRATION_TIME = 86400000;

    // Секретный ключ загружается из application.properties
    private final Key key;

    public JwtCore(@Value("${jwt.secret}") String secret) {
        // Декодируем Base64 строку в ключ
        byte[] keyBytes = Base64.getDecoder().decode(secret);
        this.key = new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());
    }

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