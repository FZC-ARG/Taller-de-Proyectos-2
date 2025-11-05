package com.appmartin.desmartin.service;

import com.appmartin.desmartin.dto.*;
import com.appmartin.desmartin.model.*;
import com.appmartin.desmartin.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TestService {
    
    @Autowired
    private PreguntaTestRepository preguntaTestRepository;
    
    @Autowired
    private AlumnoRepository alumnoRepository;
    
    @Autowired
    private IntentoTestRepository intentoTestRepository;
    
    @Autowired
    private RespuestaAlumnoRepository respuestaAlumnoRepository;
    
    @Autowired
    private ResultadoTestRepository resultadoTestRepository;
    
    @Autowired
    private TipoInteligenciaRepository tipoInteligenciaRepository;
    
    public List<PreguntaDTO> obtenerPreguntas() {
        return preguntaTestRepository.findAll().stream()
            .map(p -> new PreguntaDTO(
                p.getIdPregunta(),
                p.getTipoInteligencia().getIdInteligencia(),
                p.getTipoInteligencia().getNombre(),
                p.getTextoPregunta()
            ))
            .collect(Collectors.toList());
    }
    
    @Transactional
    public void completarTest(CompletarTestRequest request) {
        // Obtener alumno
        Alumno alumno = alumnoRepository.findById(request.getIdAlumno())
            .orElseThrow(() -> new RuntimeException("Alumno no encontrado"));
        
        // Crear intento de test
        IntentoTest intento = new IntentoTest();
        intento.setAlumno(alumno);
        IntentoTest intentoSaved = intentoTestRepository.save(intento);
        
        // Guardar respuestas individuales
        for (CompletarTestRequest.RespuestaRequest respuestaRequest : request.getRespuestas()) {
            PreguntaTest pregunta = preguntaTestRepository.findById(respuestaRequest.getIdPregunta())
                .orElseThrow(() -> new RuntimeException("Pregunta no encontrada"));
            
            RespuestaAlumno respuesta = new RespuestaAlumno();
            respuesta.setIntentoTest(intentoSaved);
            respuesta.setPreguntaTest(pregunta);
            respuesta.setPuntaje(respuestaRequest.getPuntaje().byteValue());
            
            respuestaAlumnoRepository.save(respuesta);
        }
        
        // Calcular puntajes por tipo de inteligencia
        Map<Integer, Float> puntajesPorInteligencia = calcularPuntajes(request.getRespuestas());
        
        // Guardar resultados
        for (Map.Entry<Integer, Float> entry : puntajesPorInteligencia.entrySet()) {
            TipoInteligencia tipoInteligencia = tipoInteligenciaRepository.findById(entry.getKey())
                .orElseThrow(() -> new RuntimeException("Tipo de inteligencia no encontrado"));
            
            ResultadoTest resultado = new ResultadoTest();
            resultado.setIntentoTest(intentoSaved);
            resultado.setTipoInteligencia(tipoInteligencia);
            resultado.setPuntajeCalculado(entry.getValue());
            
            resultadoTestRepository.save(resultado);
        }
    }
    
    private Map<Integer, Float> calcularPuntajes(List<CompletarTestRequest.RespuestaRequest> respuestas) {
        Map<Integer, List<Integer>> puntajesPorInteligencia = new HashMap<>();
        
        for (CompletarTestRequest.RespuestaRequest respuesta : respuestas) {
            PreguntaTest pregunta = preguntaTestRepository.findById(respuesta.getIdPregunta())
                .orElseThrow(() -> new RuntimeException("Pregunta no encontrada"));
            
            Integer idInteligencia = pregunta.getTipoInteligencia().getIdInteligencia();
            puntajesPorInteligencia.computeIfAbsent(idInteligencia, k -> new java.util.ArrayList<>())
                .add(respuesta.getPuntaje());
        }
        
        Map<Integer, Float> promedios = new HashMap<>();
        for (Map.Entry<Integer, List<Integer>> entry : puntajesPorInteligencia.entrySet()) {
            double promedio = entry.getValue().stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);
            promedios.put(entry.getKey(), (float) promedio);
        }
        
        return promedios;
    }
    
    public List<ResultadoDTO> obtenerUltimoResultado(Integer idAlumno) {
        List<IntentoTest> intentos = intentoTestRepository.findByAlumno_IdAlumnoOrderByFechaRealizacionDesc(idAlumno);
        
        if (intentos.isEmpty()) {
            return List.of();
        }
        
        IntentoTest ultimoIntento = intentos.get(0);
        List<ResultadoTest> resultados = resultadoTestRepository.findByIntentoId(ultimoIntento.getIdIntento());
        
        return resultados.stream()
            .map(r -> new ResultadoDTO(
                r.getIdResultado(),
                r.getIntentoTest().getIdIntento(),
                r.getIntentoTest().getFechaRealizacion(),
                r.getTipoInteligencia().getIdInteligencia(),
                r.getTipoInteligencia().getNombre(),
                r.getPuntajeCalculado()
            ))
            .collect(Collectors.toList());
    }
    
    public List<ResultadoDTO> obtenerHistorialResultados(Integer idAlumno) {
        List<ResultadoTest> resultados = resultadoTestRepository.findByAlumnoId(idAlumno);
        
        return resultados.stream()
            .map(r -> new ResultadoDTO(
                r.getIdResultado(),
                r.getIntentoTest().getIdIntento(),
                r.getIntentoTest().getFechaRealizacion(),
                r.getTipoInteligencia().getIdInteligencia(),
                r.getTipoInteligencia().getNombre(),
                r.getPuntajeCalculado()
            ))
            .collect(Collectors.toList());
    }
    
    @Transactional
    public void crearResultados(CrearResultadosRequest request) {
        // Verificar que el intento existe
        IntentoTest intento = intentoTestRepository.findById(request.getIdIntento())
            .orElseThrow(() -> new RuntimeException("Intento no encontrado"));

        // Si viene idAlumno en la solicitud, validar que el intento pertenezca a ese alumno
        if (request.getIdAlumno() != null) {
            Integer alumnoIdDeIntento = intento.getAlumno().getIdAlumno();
            if (!alumnoIdDeIntento.equals(request.getIdAlumno())) {
                throw new IllegalArgumentException("El intento " + request.getIdIntento() + " no pertenece al alumno " + request.getIdAlumno());
            }
        }
        
        // Guardar cada resultado.
        for (CrearResultadosRequest.ResultadoRequest resultadoRequest : request.getResultados()) {
            // Verificar que el tipo de inteligencia existe
            TipoInteligencia tipoInteligencia = tipoInteligenciaRepository.findById(resultadoRequest.getIdInteligencia())
                .orElseThrow(() -> new RuntimeException("Tipo de inteligencia no encontrado: " + resultadoRequest.getIdInteligencia()));
            
            // Crear y guardar el resultado
            ResultadoTest resultado = new ResultadoTest();
            resultado.setIntentoTest(intento);
            resultado.setTipoInteligencia(tipoInteligencia);
            resultado.setPuntajeCalculado(resultadoRequest.getPuntajeCalculado());
            
            resultadoTestRepository.save(resultado);
        }
    }
    
    public CrearResultadosRequest obtenerResultadosPorAlumno(Integer idAlumno) {
        // Obtener el último intento del alumno
        List<IntentoTest> intentos = intentoTestRepository.findByAlumno_IdAlumnoOrderByFechaRealizacionDesc(idAlumno);
        
        if (intentos.isEmpty()) {
            throw new RuntimeException("No se encontraron intentos para el alumno con ID: " + idAlumno);
        }
        
        IntentoTest ultimoIntento = intentos.get(0);
        
        // Obtener los resultados del último intento
        List<ResultadoTest> resultados = resultadoTestRepository.findByIntentoId(ultimoIntento.getIdIntento());
        
        if (resultados.isEmpty()) {
            throw new RuntimeException("No se encontraron resultados para el intento con ID: " + ultimoIntento.getIdIntento());
        }
        
        // Formatear los resultados en el formato solicitado
        List<CrearResultadosRequest.ResultadoRequest> resultadosRequest = resultados.stream()
            .map(r -> new CrearResultadosRequest.ResultadoRequest(
                r.getTipoInteligencia().getIdInteligencia(),
                r.getPuntajeCalculado()
            ))
            .collect(Collectors.toList());
        
        // Crear y retornar el objeto de respuesta
        CrearResultadosRequest response = new CrearResultadosRequest();
        response.setIdIntento(ultimoIntento.getIdIntento());
        response.setResultados(resultadosRequest);
        
        return response;
    }
}

