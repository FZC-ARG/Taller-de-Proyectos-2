package com.prsanmartin.appmartin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Usar factor de fuerza 12
        return new BCryptPasswordEncoder(12);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // **********************************************
        // DESACTIVACIÓN TOTAL DE SEGURIDAD PARA PRUEBAS
        // **********************************************
        return http
            .csrf(csrf -> csrf.disable()) // 1. Deshabilitar CSRF (Causa de 403 en POST/PUT/DELETE)
            .cors(cors -> cors.disable()) // 2. Deshabilitar CORS configurado en Security
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll() // 3. Permitir TODAS las peticiones
            )
            .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable())) // 4. Deshabilitar frameOptions (útil para Swagger/H2 Console)
            // Se asume que los filtros JWT y @PreAuthorize ya están comentados en otras clases.
            .build();
    }
}