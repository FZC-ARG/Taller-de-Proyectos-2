package com.prsanmartin.appmartin.controller;

import com.prsanmartin.appmartin.dto.LoginRequest;

import com.prsanmartin.appmartin.dto.RefreshTokenRequest;

import com.prsanmartin.appmartin.entity.RefreshToken;
import com.prsanmartin.appmartin.service.AuditoriaService;
import com.prsanmartin.appmartin.service.RateLimitService;
import com.prsanmartin.appmartin.service.RefreshTokenService;
import com.prsanmartin.appmartin.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private AuditoriaService auditoriaService;

    @Autowired
    private RateLimitService rateLimitService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        String clientIP = getClientIP(request);
        Map<String, Object> response = new HashMap<>();

        // Rate limiting
        if (!rateLimitService.isLoginAllowed(clientIP) || !rateLimitService.isLoginAllowedByUser(loginRequest.getUsuario())) {
            response.put("exito", false);
            response.put("mensaje", "Demasiados intentos de login. Intente más tarde.");
            auditoriaService.registrarAccion("LOGIN_BLOQUEADO", "USUARIO", null, "Rate limit excedido", clientIP, loginRequest.getUsuario());
            return ResponseEntity.status(429).body(response);
        }

        try {

            response.put("exito", false);
            response.put("mensaje", "Funcionalidad de autenticación no implementada.");
            return ResponseEntity.status(501).body(response);
        } catch (Exception e) {
            auditoriaService.registrarLogin(loginRequest.getUsuario(), clientIP, false);
            response.put("exito", false);
            response.put("mensaje", "Error interno del servidor: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<Map<String, Object>> refresh(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest, HttpServletRequest request) {
        String clientIP = getClientIP(request);
        Map<String, Object> response = new HashMap<>();

        try {
            RefreshToken refreshToken = refreshTokenService.verificarRefreshToken(refreshTokenRequest.getRefreshToken());
            
            // Generar nuevo access token
            String newAccessToken = jwtUtil.generateToken(
                refreshToken.getUsuario().getNombreUsuario(),
                refreshToken.getUsuario().getIdUsuario(),
                refreshToken.getUsuario().getNombreUsuario(),
                refreshToken.getUsuario().getRol().getNombreRol()
            );

            // Crear nuevo refresh token (rotación)
            RefreshToken newRefreshToken = refreshTokenService.crearRefreshToken(refreshToken.getUsuario().getNombreUsuario());
            
            // Revocar el token anterior
            refreshTokenService.revocarToken(refreshTokenRequest.getRefreshToken());

            response.put("exito", true);
            response.put("accessToken", newAccessToken);
            response.put("refreshToken", newRefreshToken.getToken());
            response.put("tipoToken", "Bearer");
            response.put("mensaje", "Token renovado exitosamente");

            auditoriaService.registrarAccion("TOKEN_RENOVADO", "USUARIO", refreshToken.getUsuario().getIdUsuario(), "Refresh token rotado", clientIP, refreshToken.getUsuario().getNombreUsuario());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("exito", false);
            response.put("mensaje", "Token de renovación inválido: " + e.getMessage());
            return ResponseEntity.status(401).body(response);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(@RequestBody(required = false) RefreshTokenRequest refreshTokenRequest, HttpServletRequest request) {
        String clientIP = getClientIP(request);
        Map<String, Object> response = new HashMap<>();

        try {
            if (refreshTokenRequest != null && refreshTokenRequest.getRefreshToken() != null) {
                refreshTokenService.revocarToken(refreshTokenRequest.getRefreshToken());
            }

            response.put("exito", true);
            response.put("mensaje", "Logout exitoso");

            // Registrar logout en auditoría si tenemos el usuario del token
            if (refreshTokenRequest != null && refreshTokenRequest.getRefreshToken() != null) {
                try {
                    RefreshToken token = refreshTokenService.findByToken(refreshTokenRequest.getRefreshToken()).orElse(null);
                    if (token != null) {
                        auditoriaService.registrarLogout(token.getUsuario().getNombreUsuario(), clientIP);
                    }
                } catch (Exception e) {
                    // Token inválido, continuar con logout
                }
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("exito", false);
            response.put("mensaje", "Error durante logout: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> status() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "active");
        response.put("mensaje", "Servicio de autenticación activo");
        response.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(response);
    }

    private String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}
