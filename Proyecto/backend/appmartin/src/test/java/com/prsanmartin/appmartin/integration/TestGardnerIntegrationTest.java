package com.prsanmartin.appmartin.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prsanmartin.appmartin.dto.*;
import com.prsanmartin.appmartin.entity.*;
import com.prsanmartin.appmartin.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.*;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class TestGardnerIntegrationTest {
    
    @Autowired
    private WebApplicationContext context;
    
    @Autowired
    private TestGardnerRepository testRepository;
    
    @Autowired
    private AlumnoRepository alumnoRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private RolRepository rolRepository;
    
    @Autowired
    private PreguntaGardnerRepository preguntaRepository;
    
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    
    private Alumno testAlumno;
    private Usuario testUsuario;
    private Rol alumnoRol;
    
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
        
        objectMapper = new ObjectMapper();
        
        createTestData();
    }
    
    @Test
    @WithMockUser(authorities = "ROLE_ALUMNO")
    void testGetAllQuestions_ShouldReturnActiveQuestions() throws Exception {
        mockMvc.perform(get("/api/test-gardner/questions/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(32)); // 32 questions in migration
    }
    
    @Test
    @WithMockUser(authorities = "ROLE_ALUMNO")
    void testAutosaveTest_ShouldCreateNewDraft() throws Exception {
        AutosaveRequestDTO request = createValidAutosaveRequest();
        
        mockMvc.perform(post("/api/test-gardner/{idAlumno}/autosave", testAlumno.getIdAlumno())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("saved"))
                .andExpect(jsonPath("$.idTest").exists())
                .andExpect(jsonPath("$.version").value(1))
                .andExpect(jsonPath("$.estado").value("BORRADOR"));
    }
    
    @Test
    @WithMockUser(authorities = "ROLE_ALUMNO")
    void testSubmitTest_ShouldCalculateAndSaveResults() throws Exception {
        AutosaveRequestDTO request = createValidAutosaveRequest();
        request.setEstado("FINAL");
        
        mockMvc.perform(post("/api/test-gardner/{idAlumno}/submit", testAlumno.getIdAlumno())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idTest").exists())
                .andExpect(jsonPath("$.idAlumno").value(testAlumno.getIdAlumno()))
                .andExpect(jsonPath("$.inteligenciaPredominante").exists())
                .andExpect(jsonPath("$.puntajeTotal").isNumber())
                .andExpect(jsonPath("$.estado").value("CALCULADO"))
                .andExpect(jsonPath("$.descripcionInteligencia").exists())
                .andExpect(jsonPath("$.recomendacionesAcademicas").exists());
    }
    
    @Test
    @WithMockUser(authorities = "ROLE_ALUMNO")
    void testGetTestHistory_ShouldReturnPreviousResults() throws Exception {
        // First submit a test
        AutosaveRequestDTO request = createValidAutosaveRequest();
        request.setEstado("FINAL");
        
        mockMvc.perform(post("/api/test-gardner/{idAlumno}/submit", testAlumno.getIdAlumno())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));
        
        // Then get history
        mockMvc.perform(get("/api/test-gardner/{idAlumno}/history", testAlumno.getIdAlumno()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].idAlumno").value(testAlumno.getIdAlumno()))
                .andExpect(jsonPath("$[0].estado").value("CALCULADO"));
    }
    
    @Test
    @WithMockUser(authorities = "ROLE_ALUMNO")
    void testCanStudentTakeTest_WithNoPreviousTests_ShouldReturnTrue() throws Exception {
        mockMvc.perform(get("/api/test-gardner/{idAlumno}/can-take", testAlumno.getIdAlumno()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.canTake").value(true))
                .andExpect(jsonPath("$.message").value("Puede tomar el test"));
    }
    
    @Test
    @WithMockUser(authorities = "ROLE_DOCENTE")
    void testGetIntelligenceTypeStatistics_ShouldReturnCounts() throws Exception {
        mockMvc.perform(get("/api/test-gardner/statistics/intelligence-types"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.musical").exists())
                .andExpect(jsonPath("$.logico_matematico").exists())
                .andExpect(jsonPath("$.espacial").exists())
                .andExpect(jsonPath("$.linguistico").exists());
    }
    
    @Test
    @WithMockUser(authorities = "ROLE_ALUMNO")
    void testAutosaveWithInvalidRequest_ShouldReturnBadRequest() throws Exception {
        AutosaveRequestDTO invalidRequest = new AutosaveRequestDTO();
        invalidRequest.setIdAlumno(999); // Non-existent student
        invalidRequest.setRespuestas(Arrays.asList()); // Empty responses
        
        mockMvc.perform(post("/api/test-gardner/{idAlumno}/autosave", testAlumno.getIdAlumno())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void testAccessDeniedForAdmin_ShouldReturnForbidden() throws Exception {
        mockMvc.perform(get("/api/test-gardner/questions/all"))
                .andExpect(status().isForbidden());
    }
    
    @Test
    @WithMockUser(authorities = "ROLE_ALUMNO")
    void testGetQuestionByType_ShouldReturnFilteredQuestions() throws Exception {
        mockMvc.perform(get("/api/test-gardner/questions/by-type/MUSICAL"))
                .andExpect(status().isBadRequest()); // This should be musical (lowercase)
    }
    
    @Test
    @WithMockUser(authorities = "ROLE_ALUMNO")
    void testGetQuestionByTypeCorrectCase_ShouldReturnFilteredQuestions() throws Exception {
        mockMvc.perform(get("/api/test-gardner/questions/by-type/musical"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].tipoInteligencia").value("musical"));
    }
    
    @Test
    @WithMockUser(authorities = "ROLE_ALUMNO")
    void testAutosaveWithDuplicateRequestId_ShouldReturnDuplicateResponse() throws Exception {
        AutosaveRequestDTO request = createValidAutosaveRequest();
        request.setClientRequestId("duplicate-test-id");
        
        // First request
        mockMvc.perform(post("/api/test-gardner/{idAlumno}/autosave", testAlumno.getIdAlumno())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("saved"));
        
        // Duplicate request
        mockMvc.perform(post("/api/test-gardner/{idAlumno}/autosave", testAlumno.getIdAlumno())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("duplicate"));
    }
    
    private void createTestData() {
        // Create role
        alumnoRol = rolRepository.findByNombreRol("ALUMNO");
        if (alumnoRol == null) {
            alumnoRol = new Rol();
            alumnoRol.setNombreRol("ALUMNO");
            alumnoRol = rolRepository.save(alumnoRol);
        }
        
        // Create test user
        testUsuario = new Usuario();
        testUsuario.setNombreUsuario("testuser123");
        testUsuario.setCorreoElectronico("testuser123@example.com");
        testUsuario.setContrasenaHash("$2a$10$test.hash");
        testUsuario.setRol(alumnoRol);
        testUsuario.setFechaCreacion(LocalDateTime.now());
        testUsuario = usuarioRepository.save(testUsuario);
        
        // Create test student
        testAlumno = new Alumno();
        testAlumno.setUsuario(testUsuario);
        testAlumno.setAnioIngreso(2023);
        testAlumno = alumnoRepository.save(testAlumno);
    }
    
    private AutosaveRequestDTO createValidAutosaveRequest() {
        AutosaveRequestDTO request = new AutosaveRequestDTO();
        request.setClientRequestId("integration-test-uuid");
        request.setIdAlumno(testAlumno.getIdAlumno());
        request.setEstado("BORRADOR");
        
        // Create valid responses based on first 3 questions (which will exist in the migration)
        AutosaveRequestDTO.RespuestaTestDTO resp1 = new AutosaveRequestDTO.RespuestaTestDTO();
        resp1.setIdPregunta(1);
        resp1.setOpcionSeleccionada(1);
        
        AutosaveRequestDTO.RespuestaTestDTO resp2 = new AutosaveRequestDTO.RespuestaTestDTO();
        resp2.setIdPregunta(2);
        resp2.setOpcionSeleccionada(2);
        
        AutosaveRequestDTO.RespuestaTestDTO resp3 = new AutosaveRequestDTO.RespuestaTestDTO();
        resp3.setIdPregunta(3);
        resp3.setOpcionSeleccionada(3);
        
        request.setRespuestas(Arrays.asList(resp1, resp2, resp3));
        
        return request;
    }
}
