package com.prsanmartin.appmartin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prsanmartin.appmartin.dto.LoginRequest;
import com.prsanmartin.appmartin.dto.RefreshTokenRequest;
import com.prsanmartin.appmartin.service.AdministradorService;
import com.prsanmartin.appmartin.service.AuditoriaService;
import com.prsanmartin.appmartin.service.RateLimitService;
import com.prsanmartin.appmartin.service.RefreshTokenService;
import com.prsanmartin.appmartin.util.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdministradorService administradorService;

    @MockBean
    private RefreshTokenService refreshTokenService;

    @MockBean
    private AuditoriaService auditoriaService;

    @MockBean
    private RateLimitService rateLimitService;

    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testLoginSuccess() throws Exception {
        // Given
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsuario("admin1");
        loginRequest.setContrasena("password123");

        when(rateLimitService.isLoginAllowed(anyString())).thenReturn(true);
        when(rateLimitService.isLoginAllowedByUser(anyString())).thenReturn(true);
        when(administradorService.autenticarAdministrador(any(LoginRequest.class)))
            .thenReturn(createMockLoginResponse(true));

        // When & Then
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exito").value(true))
                .andExpect(jsonPath("$.mensaje").value("Autenticación exitosa"));
    }

    @Test
    void testLoginFailure() throws Exception {
        // Given
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsuario("admin1");
        loginRequest.setContrasena("wrongpassword");

        when(rateLimitService.isLoginAllowed(anyString())).thenReturn(true);
        when(rateLimitService.isLoginAllowedByUser(anyString())).thenReturn(true);
        when(administradorService.autenticarAdministrador(any(LoginRequest.class)))
            .thenReturn(createMockLoginResponse(false));

        // When & Then
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.exito").value(false));
    }

    @Test
    void testRateLimitExceeded() throws Exception {
        // Given
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsuario("admin1");
        loginRequest.setContrasena("password123");

        when(rateLimitService.isLoginAllowed(anyString())).thenReturn(false);

        // When & Then
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isTooManyRequests())
                .andExpect(jsonPath("$.exito").value(false))
                .andExpect(jsonPath("$.mensaje").value("Demasiados intentos de login. Intente más tarde."));
    }

    @Test
    void testRefreshTokenSuccess() throws Exception {
        // Given
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest();
        refreshTokenRequest.setRefreshToken("valid-refresh-token");

        when(refreshTokenService.verificarRefreshToken(anyString()))
            .thenReturn(createMockRefreshToken());

        // When & Then
        mockMvc.perform(post("/api/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(refreshTokenRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exito").value(true))
                .andExpect(jsonPath("$.mensaje").value("Token renovado exitosamente"));
    }

    @Test
    void testLogoutSuccess() throws Exception {
        // Given
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest();
        refreshTokenRequest.setRefreshToken("valid-refresh-token");

        // When & Then
        mockMvc.perform(post("/api/auth/logout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(refreshTokenRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exito").value(true))
                .andExpect(jsonPath("$.mensaje").value("Logout exitoso"));
    }

    @Test
    void testStatusEndpoint() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/auth/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("active"))
                .andExpect(jsonPath("$.mensaje").value("Servicio de autenticación activo"));
    }

    private com.prsanmartin.appmartin.dto.LoginResponse createMockLoginResponse(boolean success) {
        com.prsanmartin.appmartin.dto.LoginResponse response = new com.prsanmartin.appmartin.dto.LoginResponse();
        response.setExito(success);
        if (success) {
            response.setToken("mock-jwt-token");
            response.setIdAdministrador(1);
            response.setNombreCompleto("Admin Test");
            response.setUsuario("admin1");
            response.setCorreoElectronico("admin1@prmartin.com");
            response.setMensaje("Autenticación exitosa");
        } else {
            response.setMensaje("Credenciales inválidas");
        }
        return response;
    }

    private com.prsanmartin.appmartin.entity.RefreshToken createMockRefreshToken() {
        com.prsanmartin.appmartin.entity.RefreshToken token = new com.prsanmartin.appmartin.entity.RefreshToken();
        token.setToken("mock-refresh-token");
        // Add other necessary fields
        return token;
    }
}
