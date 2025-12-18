package com.deliveryapp.backend.config;

import com.deliveryapp.backend.security.TokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final TokenFilter tokenFilter;

    public SecurityConfig(TokenFilter tokenFilter) {
        this.tokenFilter = tokenFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Отключаем защиту CSRF (она не нужна для REST API)
                .csrf(AbstractHttpConfigurer::disable)
                // Разрешаем CORS (чтобы фронтенд мог слать запросы)
                .cors(httpSecurityCorsConfigurer ->
                        httpSecurityCorsConfigurer.configurationSource(request ->
                                new CorsConfiguration().applyPermitDefaultValues()
                        )
                )
                // Если пользователь не вошел - возвращаем 401
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                )
                // Не сохраняем сессию на сервере (Stateless)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // ПРАВИЛА ДОСТУПА
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/ws/**", "/app/**", "/topic/**").permitAll() // <-- Вход и регистрация доступны ВСЕМ
                        .requestMatchers("/*.html", "/css/**", "/js/**", "/static/**").permitAll()
                        .anyRequest().authenticated() // <-- Все остальное - только с токеном
                )
                // Добавляем наш фильтр перед стандартным фильтром
                .addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        // Создаем экземпляр кодировщика BCrypt.
        // Это стандарт индустрии. Он специально медленный, чтобы хакеры не могли быстро перебирать пароли.
        return new BCryptPasswordEncoder();
    }
    //(ИСПРАВЛЕНИЕ CORS)
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // ВМЕСТО .setAllowedOrigins(Arrays.asList("*"));
        // ИСПОЛЬЗУЙТЕ Patterns:
        configuration.setAllowedOriginPatterns(List.of("*")); // <-- Разрешает всем, но безопасно

        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true); // <-- Это то, из-за чего была ошибка

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}