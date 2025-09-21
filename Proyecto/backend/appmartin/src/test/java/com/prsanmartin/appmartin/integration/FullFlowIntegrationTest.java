/*
package com.prsanmartin.appmartin.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prsanmartin.appmartin.dto.LoginRequest;
import com.prsanmartin.appmartin.dto.LoginResponse;
import com.prsanmartin.appmartin.dto.CursoDTO;
import com.prsanmartin.appmartin.dto.MatriculaDTO;
import com.prsanmartin.appmartin.entity.Usuario;
import com.prsanmartin.appmartin.entity.Rol;
import com.prsanmartin.appmartin.repository.UsuarioRepository;
import com.prsanmartin.appmartin.repository.RolRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
@ActiveProfiles("test")
@Testcontainers
public class FullFlowIntegrationTest {

    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test")
            .withInitScript("db/migration/V1__init.sql");

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    private MockMvc mockMvc;
    private String accessToken;
    private String refreshToken;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        
        // Create test roles and users
        Rol adminRol = new Rol(1, "ADMIN");
        Rol docenteRol = new Rol(2, "DOCENTE");
        Rol alumnoRol = new Rol(3, "ALUMNO");
        
        rolRepository.save(adminRol);
        rolRepository.save(docenteRol);
        rolRepository.save(alumnoRol);
        
        Usuario admin = new Usuario(1, "admin1", "admin1@example.com", "$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewdBPj4J/8QzKz2O", adminRol, LocalDateTime.now());
        Usuario docente = new Usuario(2, "docente1", "docente1@example.com", "$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewdBPj4J/8QzKz2O", docenteRol, LocalDateTime.now());
        Usuario alumno = new Usuario(3, "alumno1", "alumno1@example.com", "$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewdBPj4J/8QzKz2O", alumnoRol, LocalDateTime.now());
        
        usuarioRepository.save(admin);
        usuarioRepository.save(docente);
        usuarioRepository.save(alumno);
    }

    @Test
    void testFullFlowIntegration() throws Exception {
        // Step 1: Login as admin
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsuario("admin1");
        loginRequest.setContrasena("password123");

        String loginResponse = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exito").value(true))
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.refreshToken").exists())
                .andReturn()
                .getResponse()
                .getContentAsString();

        LoginResponse loginResponseObj = objectMapper.readValue(loginResponse, LoginResponse.class);
        accessToken = loginResponseObj.getAccessToken();
        refreshToken = loginResponseObj.getRefreshToken();

        // Step 2: Create a course
        CursoDTO cursoDTO = new CursoDTO();
        cursoDTO.setNombre("Matemáticas Avanzadas");
        cursoDTO.setDescripcion("Curso de matemáticas para estudiantes avanzados");
        cursoDTO.setIdDocente(2L); // docente1
        cursoDTO.setCreditos(4);
        cursoDTO.setEstado("ACTIVO");

        String cursoResponse = mockMvc.perform(post("/api/cursos")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cursoDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Matemáticas Avanzadas"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        // Step 3: Enroll student in course
        MatriculaDTO matriculaDTO = new MatriculaDTO();
        matriculaDTO.setIdAlumno(3L); // alumno1
        matriculaDTO.setIdCurso(1L); // Assuming the course gets ID 1
        matriculaDTO.setFechaMatricula(LocalDateTime.now());
        matriculaDTO.setEstado("ACTIVA");
        matriculaDTO.setObservaciones("Matrícula inicial");

        mockMvc.perform(post("/api/matriculas")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(matriculaDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.estado").value("ACTIVA"));

        // Step 4: Get all courses
        mockMvc.perform(get("/api/cursos")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].nombre").value("Matemáticas Avanzadas"));

        // Step 5: Get enrollments for student
        mockMvc.perform(get("/api/matriculas/alumno/3")
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].estado").value("ACTIVA"));

        // Step 6: Refresh token
        mockMvc.perform(post("/api/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"refreshToken\":\"" + refreshToken + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exito").value(true))
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.refreshToken").exists());

        // Step 7: Logout
        mockMvc.perform(post("/api/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"refreshToken\":\"" + refreshToken + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exito").value(true))
                .andExpect(jsonPath("$.mensaje").value("Logout exitoso"));
    }

    @Test
    void testUnauthorizedAccess() throws Exception {
        // Test accessing protected endpoint without token
        mockMvc.perform(get("/api/cursos"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testRoleBasedAccess() throws Exception {
        // Login as student
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsuario("alumno1");
        loginRequest.setContrasena("password123");

        String loginResponse = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        LoginResponse loginResponseObj = objectMapper.readValue(loginResponse, LoginResponse.class);
        String studentToken = loginResponseObj.getAccessToken();

        // Student should be able to view courses
        mockMvc.perform(get("/api/cursos")
                        .header("Authorization", "Bearer " + studentToken))
                .andExpect(status().isOk());

        // Student should not be able to create courses
        CursoDTO cursoDTO = new CursoDTO();
        cursoDTO.setNombre("Test Course");
        cursoDTO.setDescripcion("Test Description");
        cursoDTO.setIdDocente(2L);
        cursoDTO.setCreditos(3);
        cursoDTO.setEstado("ACTIVO");

        mockMvc.perform(post("/api/cursos")
                        .header("Authorization", "Bearer " + studentToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cursoDTO)))
                .andExpect(status().isForbidden());
    }
}
*/
