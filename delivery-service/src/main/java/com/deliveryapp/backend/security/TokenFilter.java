package com.deliveryapp.backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class TokenFilter extends OncePerRequestFilter {

    private final JwtCore jwtCore;

    public TokenFilter(JwtCore jwtCore) {
        this.jwtCore = jwtCore;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = null;
        String phoneNumber = null;
        String headerAuth = request.getHeader("Authorization");

        // Проверяем заголовок: "Authorization: Bearer <token>"
        if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
            jwt = headerAuth.substring(7); // Убираем "Bearer "
        }

        // Если токен есть, проверяем его
        if (jwt != null) {
            try {
                phoneNumber = jwtCore.getNameFromJwt(jwt);
            } catch (Exception e) {
                // Токен невалиден (истек или подделан)
                System.out.println("Ошибка валидации JWT");
            }

            // Если все ок и пользователь еще не в системе
            if (phoneNumber != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // Создаем "пропуск" для Spring Security
                // (Пока без ролей, просто пустой список authorities)
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        phoneNumber,
                        null,
                        Collections.emptyList()
                );
                // Пропускаем пользователя
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        // Передаем запрос дальше (в контроллер)
        filterChain.doFilter(request, response);
    }
}