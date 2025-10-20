package com.prsanmartin.appmartin.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.prsanmartin.appmartin.dto.RecomendacionDTO;
import com.prsanmartin.appmartin.dto.RecomendacionHistorialDTO;
import com.prsanmartin.appmartin.entity.*;
import com.prsanmartin.appmartin.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class RecomendacionServiceImpl implements RecomendacionService {
    
    @Autowired
    private HistorialRecomendacionRepository historialRepository;
    
    @Autowired
    private PlantillaRecomendacionRepository plantillaRepository;
    
    @Autowired
    private AlumnoRepository alumnoRepository;
    
    @Autowired
    private TestGardnerRepository testGardnerRepository;
    
    @Autowired
    private MatriculaRepository matriculaRepository;
    
    @Autowired
    private AuditoriaService auditoriaService;
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Override
    @Transactional
    public List<RecomendacionDTO> generateRecommendations(Integer idAlumno, Integer idTest) {
        try {
            // Get student and test
            Alumno alumno = alumnoRepository.findById(idAlumno)
                    .orElseThrow(() -> new IllegalArgumentException("Alumno no encontrado"));
            
            TestGardner test = testGardnerRepository.findById(idTest)
                    .orElseThrow(() -> new IllegalArgumentException("Test no encontrado"));
            
            if (!test.getAlumno().getIdAlumno().equals(idAlumno)) {
                throw new IllegalArgumentException("El test no pertenece al alumno especificado");
            }
            
            if (test.getEstadoGuardado() != TestGardner.EstadoGuardado.CALCULADO) {
                throw new IllegalArgumentException("El test debe estar calculado para generar recomendaciones");
            }
            
            List<RecomendacionDTO> recomendaciones = new ArrayList<>();
            
            // Parse test scores
            Map<String, Integer> puntajes = new HashMap<>();
            if (test.getPuntajes() != null) {
                try {
                    puntajes = objectMapper.readValue(test.getPuntajes(), new TypeReference<Map<String, Integer>>() {});
                } catch (Exception e) {
                    throw new IllegalArgumentException("Error al procesar puntajes del test");
                }
            }
            
            // Generate recommendations for each intelligence type
            for (Map.Entry<String, Integer> entry : puntajes.entrySet()) {
                String inteligencia = entry.getKey();
                Integer puntaje = entry.getValue();
                
                // Find matching template
                List<PlantillaRecomendacion> plantillas = plantillaRepository.findByTipoInteligenciaAndPuntajeRange(
                        convertStringToTipoInteligencia(inteligencia), puntaje);
                
                for (PlantillaRecomendacion plantilla : plantillas) {
                    // Create recommendation
                    HistorialRecomendacion historial = new HistorialRecomendacion();
                    historial.setAlumno(alumno);
                    historial.setTest(test);
                    historial.setPlantilla(plantilla);
                    historial.setInteligenciaPredominante(inteligencia);
                    historial.setPuntajeInteligencia(puntaje);
                    historial.setRecomendacionGenerada(plantilla.getContenidoRecomendacion());
                    historial.setTipoRecomendacion(HistorialRecomendacion.TipoRecomendacion.valueOf(plantilla.getTipoRecomendacion().name()));
                    
                    HistorialRecomendacion savedHistorial = historialRepository.save(historial);
                    
                    // Convert to DTO
                    RecomendacionDTO recomendacionDTO = convertToRecomendacionDTO(savedHistorial);
                    recomendaciones.add(recomendacionDTO);
                }
            }
            
            // Audit trail
            auditoriaService.registrarAccion(
                "RECOMENDACIONES_GENERADAS",
                "HistorialRecomendacion",
                null,
                "Recomendaciones generadas para alumno: " + alumno.getUsuario().getNombreUsuario(),
                null,
                "SYSTEM"
            );
            
            return recomendaciones;
            
        } catch (Exception e) {
            throw new RuntimeException("Error al generar recomendaciones: " + e.getMessage(), e);
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<RecomendacionHistorialDTO> getRecommendationHistory(Integer idAlumno) {
        List<HistorialRecomendacion> historial = historialRepository.findByAlumnoOrderByFechaGeneracionDesc(idAlumno);
        return historial.stream()
                .map(this::convertToRecomendacionHistorialDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<RecomendacionHistorialDTO> getRecommendationHistoryForTeachers(Integer docenteId) {
        // Get all students of the teacher
        List<Alumno> estudiantes = alumnoRepository.findByTeacherId(docenteId);
        List<Integer> studentIds = estudiantes.stream()
                .map(Alumno::getIdAlumno)
                .collect(Collectors.toList());
        
        List<HistorialRecomendacion> historial = historialRepository.findAll().stream()
                .filter(h -> studentIds.contains(h.getAlumno().getIdAlumno()))
                .sorted((h1, h2) -> h2.getFechaGeneracion().compareTo(h1.getFechaGeneracion()))
                .collect(Collectors.toList());
        
        return historial.stream()
                .map(this::convertToRecomendacionHistorialDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public boolean updateRecommendationStatus(Integer idHistorial, HistorialRecomendacion.EstadoRecomendacion estado, String notas) {
        try {
            HistorialRecomendacion historial = historialRepository.findById(idHistorial)
                    .orElseThrow(() -> new IllegalArgumentException("Recomendación no encontrada"));
            
            historial.setEstado(estado);
            historial.setNotas(notas);
            
            if (estado == HistorialRecomendacion.EstadoRecomendacion.aplicada) {
                historial.setFechaAplicacion(LocalDateTime.now());
            }
            
            historialRepository.save(historial);
            
            // Audit trail
            auditoriaService.registrarAccion(
                "RECOMENDACION_ACTUALIZADA",
                "HistorialRecomendacion",
                idHistorial,
                "Estado actualizado a: " + estado.name(),
                null,
                "SYSTEM"
            );
            
            return true;
            
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<PlantillaRecomendacion> getRecommendationTemplates() {
        return plantillaRepository.findActiveTemplatesOrdered();
    }
    
    @Override
    @Transactional
    public PlantillaRecomendacion createRecommendationTemplate(PlantillaRecomendacion plantilla) {
        plantilla.setActivo(true);
        return plantillaRepository.save(plantilla);
    }
    
    @Override
    @Transactional
    public PlantillaRecomendacion updateRecommendationTemplate(Integer idPlantilla, PlantillaRecomendacion plantilla) {
        PlantillaRecomendacion existingPlantilla = plantillaRepository.findById(idPlantilla)
                .orElseThrow(() -> new IllegalArgumentException("Plantilla no encontrada"));
        
        existingPlantilla.setNombrePlantilla(plantilla.getNombrePlantilla());
        existingPlantilla.setTipoInteligencia(plantilla.getTipoInteligencia());
        existingPlantilla.setPuntajeMinimo(plantilla.getPuntajeMinimo());
        existingPlantilla.setPuntajeMaximo(plantilla.getPuntajeMaximo());
        existingPlantilla.setContenidoRecomendacion(plantilla.getContenidoRecomendacion());
        existingPlantilla.setTipoRecomendacion(plantilla.getTipoRecomendacion());
        existingPlantilla.setActivo(plantilla.getActivo());
        
        return plantillaRepository.save(existingPlantilla);
    }
    
    @Override
    @Transactional
    public boolean deleteRecommendationTemplate(Integer idPlantilla) {
        try {
            PlantillaRecomendacion plantilla = plantillaRepository.findById(idPlantilla)
                    .orElseThrow(() -> new IllegalArgumentException("Plantilla no encontrada"));
            
            plantilla.setActivo(false);
            plantillaRepository.save(plantilla);
            
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getRecommendationStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        
        // Count by intelligence type
        List<Object[]> intelligenceStats = historialRepository.countByInteligenciaPredominante();
        Map<String, Long> intelligenceCount = intelligenceStats.stream()
                .collect(Collectors.toMap(
                    arr -> (String) arr[0],
                    arr -> (Long) arr[1]
                ));
        
        // Count by recommendation type
        List<Object[]> typeStats = historialRepository.countByTipoRecomendacion();
        Map<String, Long> typeCount = typeStats.stream()
                .collect(Collectors.toMap(
                    arr -> ((HistorialRecomendacion.TipoRecomendacion) arr[0]).name(),
                    arr -> (Long) arr[1]
                ));
        
        // Count by status
        List<Object[]> statusStats = historialRepository.countByEstado();
        Map<String, Long> statusCount = statusStats.stream()
                .collect(Collectors.toMap(
                    arr -> ((HistorialRecomendacion.EstadoRecomendacion) arr[0]).name(),
                    arr -> (Long) arr[1]
                ));
        
        statistics.put("totalRecommendations", historialRepository.count());
        statistics.put("intelligenceDistribution", intelligenceCount);
        statistics.put("typeDistribution", typeCount);
        statistics.put("statusDistribution", statusCount);
        statistics.put("activeTemplates", plantillaRepository.findByActivoTrue().size());
        
        return statistics;
    }
    
    @Override
    @Transactional
    public List<RecomendacionDTO> regenerateRecommendations(Integer idAlumno) {
        // Get latest test for the student
        Optional<TestGardner> latestTest = testGardnerRepository.findFirstByAlumnoIdAlumnoOrderByFechaAplicacionDesc(idAlumno);
        
        if (latestTest.isPresent()) {
            return generateRecommendations(idAlumno, latestTest.get().getIdTest());
        } else {
            throw new IllegalArgumentException("No se encontró test para el estudiante");
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<RecomendacionHistorialDTO> getRecommendationsByType(HistorialRecomendacion.TipoRecomendacion tipoRecomendacion) {
        List<HistorialRecomendacion> historial = historialRepository.findByTipoRecomendacion(tipoRecomendacion);
        return historial.stream()
                .map(this::convertToRecomendacionHistorialDTO)
                .collect(Collectors.toList());
    }
    
    // Helper methods
    private RecomendacionDTO convertToRecomendacionDTO(HistorialRecomendacion historial) {
        RecomendacionDTO dto = new RecomendacionDTO();
        dto.setIdHistorial(historial.getIdHistorial());
        dto.setIdAlumno(historial.getAlumno().getIdAlumno());
        dto.setNombreAlumno(historial.getAlumno().getUsuario().getNombreUsuario());
        dto.setIdTest(historial.getTest().getIdTest());
        dto.setInteligenciaPredominante(historial.getInteligenciaPredominante());
        dto.setPuntajeInteligencia(historial.getPuntajeInteligencia());
        dto.setRecomendacion(historial.getRecomendacionGenerada());
        dto.setTipoRecomendacion(historial.getTipoRecomendacion().name());
        dto.setFechaGeneracion(historial.getFechaGeneracion());
        dto.setEstado(historial.getEstado().name());
        dto.setNotas(historial.getNotas());
        dto.setPlantillaUsada(historial.getPlantilla().getNombrePlantilla());
        dto.setConfianza(0.85); // Default confidence
        
        return dto;
    }
    
    private RecomendacionHistorialDTO convertToRecomendacionHistorialDTO(HistorialRecomendacion historial) {
        RecomendacionHistorialDTO dto = new RecomendacionHistorialDTO();
        dto.setIdHistorial(historial.getIdHistorial());
        dto.setIdAlumno(historial.getAlumno().getIdAlumno());
        dto.setNombreAlumno(historial.getAlumno().getUsuario().getNombreUsuario());
        dto.setIdTest(historial.getTest().getIdTest());
        dto.setFechaTest(historial.getTest().getFechaAplicacion());
        dto.setInteligenciaPredominante(historial.getInteligenciaPredominante());
        dto.setPuntajeInteligencia(historial.getPuntajeInteligencia());
        dto.setRecomendacionGenerada(historial.getRecomendacionGenerada());
        dto.setTipoRecomendacion(historial.getTipoRecomendacion().name());
        dto.setFechaGeneracion(historial.getFechaGeneracion());
        dto.setFechaAplicacion(historial.getFechaAplicacion());
        dto.setEstado(historial.getEstado().name());
        dto.setNotas(historial.getNotas());
        dto.setPlantillaUsada(historial.getPlantilla().getNombrePlantilla());
        
        return dto;
    }
    
    private PlantillaRecomendacion.TipoInteligencia convertStringToTipoInteligencia(String inteligencia) {
        try {
            return PlantillaRecomendacion.TipoInteligencia.valueOf(inteligencia);
        } catch (IllegalArgumentException e) {
            // Map from test intelligence names to template intelligence names
            Map<String, PlantillaRecomendacion.TipoInteligencia> mapping = Map.of(
                "musical", PlantillaRecomendacion.TipoInteligencia.musical,
                "logico", PlantillaRecomendacion.TipoInteligencia.logico_matematico,
                "espacial", PlantillaRecomendacion.TipoInteligencia.espacial,
                "linguistico", PlantillaRecomendacion.TipoInteligencia.linguistico,
                "corporal", PlantillaRecomendacion.TipoInteligencia.corporal_cinestesico,
                "interpersonal", PlantillaRecomendacion.TipoInteligencia.interpersonal,
                "intrapersonal", PlantillaRecomendacion.TipoInteligencia.intrapersonal,
                "naturalista", PlantillaRecomendacion.TipoInteligencia.naturalista
            );
            return mapping.getOrDefault(inteligencia, PlantillaRecomendacion.TipoInteligencia.linguistico);
        }
    }
}
