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
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String jwt = null;
        String phoneNumber = null;
        String headerAuth = request.getHeader("Authorization");

        // DEBUG: Логируем что пришло
        System.out.println("=== TokenFilter DEBUG ===");
        System.out.println("URL: " + request.getRequestURI());

        // 1. ПРОВЕРКА ЗАГОЛОВКА (для обычных REST запросов)
        if (headerAuth != null && headerAuth.startsWith("Bearer ")) {
            jwt = headerAuth.substring(7);
            System.out.println("JWT extracted from Header");
        }
        // 2. НОВАЯ ПРОВЕРКА: Ищем токен в URL (для WebSocket)
        // Сработает, если заголовка нет, но в ссылке есть ?token=...
        else {
            String paramToken = request.getParameter("token");
            if (paramToken != null) {
                jwt = paramToken;
                System.out.println("JWT extracted from URL parameter");
            } else {
                System.out.println("No Token found in Header or URL!");
            }
        }

        // 3. ОБЩАЯ ВАЛИДАЦИЯ (код ниже остался прежним)
        if (jwt != null) {
            try {
                phoneNumber = jwtCore.getNameFromJwt(jwt);
                System.out.println("Token valid! Phone: " + phoneNumber);
            } catch (Exception e) {
                System.out.println("Ошибка валидации JWT: " + e.getClass().getSimpleName());
            }

            if (phoneNumber != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        phoneNumber,
                        null,
                        Collections.emptyList());
                SecurityContextHolder.getContext().setAuthentication(auth);
                System.out.println("Authentication set successfully!");
            }
        }
        System.out.println("=========================");

        filterChain.doFilter(request, response);
    }
}