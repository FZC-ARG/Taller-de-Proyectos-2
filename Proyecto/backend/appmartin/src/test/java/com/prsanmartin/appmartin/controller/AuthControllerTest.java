package com.prsanmartin.appmartin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prsanmartin.appmartin.dto.LoginRequest;
import com.prsanmartin.appmartin.dto.RefreshTokenRequest;
import com.prsanmartin.appmartin.entity.RefreshToken;
import com.prsanmartin.appmartin.entity.Rol;
import com.prsanmartin.appmartin.entity.Usuario;
import com.prsanmartin.appmartin.repository.UsuarioRepository;
import com.prsanmartin.appmartin.service.AuditoriaService;
import com.prsanmartin.appmartin.service.RateLimitService;
import com.prsanmartin.appmartin.service.RefreshTokenService;
import com.prsanmartin.appmartin.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
@Import({JwtUtil.class, RateLimitService.class})
@ActiveProfiles("test")
class AuthControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RefreshTokenService refreshTokenService;

    @MockBean
    private AuditoriaService auditoriaService;

    @MockBean
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RateLimitService rateLimitService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    private Usuario testUser;
    private Rol adminRol;

    @BeforeEach
    void setUp() {
    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        adminRol = new Rol(1, "ADMIN");
        testUser = new Usuario();
        testUser.setIdUsuario(1); // usa el nombre real del setter en tu entidad
        testUser.setNombreUsuario("testuser");
        testUser.setCorreoElectronico("test@example.com");
        testUser.setContrasenaHash("hashedpw");
        testUser.setRol(adminRol); // rol debe estar instanciado
        testUser.setFechaCreacion(LocalDateTime.now());
        testUser.setActivo(true);

        // CORRECCIÓN: El mock debe devolver Optional<Usuario>, no Optional<String>
        when(usuarioRepository.findByNombreUsuario(anyString())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        // Mock RefreshTokenService
        RefreshToken mockRefreshToken = new RefreshToken(
            1, // id
            "valid-refresh-token", // token
            testUser, // usuario
            Instant.now(), // fechaCreacion
            Instant.now().plusSeconds(3600), // fechaExpiracion
            false // usado
        );

        when(refreshTokenService.crearRefreshToken(anyString())).thenReturn(mockRefreshToken);
        when(refreshTokenService.verificarRefreshToken(anyString())).thenReturn(mockRefreshToken);
        when(refreshTokenService.findByToken(anyString())).thenReturn(Optional.of(mockRefreshToken));

        // Reset rate limit buckets for each test
        rateLimitService.resetAllBuckets();
    }

    @Test
    void testLoginNotImplemented() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsuario("testUser");
        loginRequest.setContrasena("password123");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isNotImplemented())
            .andExpect(jsonPath("$.exito").value(false))
            .andExpect(jsonPath("$.mensaje").value("Funcionalidad de autenticación no implementada."));
    }

    @Test
    void testRateLimitExceeded() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsuario("testUser");
        loginRequest.setContrasena("password123");

        // Exceed the rate limit (5 attempts per 15 minutes for IP)
        for (int i = 0; i < 5; i++) {
            rateLimitService.isLoginAllowed("127.0.0.1");
        }

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isTooManyRequests())
            .andExpect(jsonPath("$.exito").value(false))
            .andExpect(jsonPath("$.mensaje").value("Demasiados intentos de login. Intente más tarde."));
    }

    @Test
    void testRefreshTokenSuccess() throws Exception {
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest();
        refreshTokenRequest.setRefreshToken("valid-refresh-token");

        mockMvc.perform(post("/api/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(refreshTokenRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.exito").value(true))
            .andExpect(jsonPath("$.accessToken").exists())
            .andExpect(jsonPath("$.refreshToken").exists());
    }

    @Test
    void testLogoutSuccess() throws Exception {
        RefreshTokenRequest refreshTokenRequest = new RefreshTokenRequest();
        refreshTokenRequest.setRefreshToken("valid-refresh-token");

        mockMvc.perform(post("/api/auth/logout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(refreshTokenRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.exito").value(true))
            .andExpect(jsonPath("$.mensaje").value("Logout exitoso"));
    }

    @Test
    void testStatusEndpoint() throws Exception {
        mockMvc.perform(get("/api/auth/status"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("active"));
    }
}
