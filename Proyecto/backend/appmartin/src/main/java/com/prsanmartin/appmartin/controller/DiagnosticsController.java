package com.prsanmartin.appmartin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Controlador de diagnóstico para listar todos los endpoints mapeados
 * Útil para verificar que no hay conflictos de rutas
 */
@RestController
@RequestMapping("/api/diagnostics")
@CrossOrigin(origins = "*")
public class DiagnosticsController {

    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @GetMapping("/mappings")
    public Map<String, Object> getAllMappings() {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> mappings = new HashMap<>();
        
        try {
            Map<RequestMappingInfo, org.springframework.web.method.HandlerMethod> handlerMethods = 
                requestMappingHandlerMapping.getHandlerMethods();
            
            for (Map.Entry<RequestMappingInfo, org.springframework.web.method.HandlerMethod> entry : handlerMethods.entrySet()) {
                RequestMappingInfo mappingInfo = entry.getKey();
                org.springframework.web.method.HandlerMethod handlerMethod = entry.getValue();
                
                Set<String> patterns = mappingInfo.getPatternsCondition().getPatterns();
                Set<RequestMethod> methods = mappingInfo.getMethodsCondition().getMethods();
                
                for (String pattern : patterns) {
                    for (RequestMethod method : methods) {
                        String key = method.name() + " " + pattern;
                        mappings.put(key, Map.of(
                            "handler", handlerMethod.getBeanType().getSimpleName() + "." + handlerMethod.getMethod().getName(),
                            "class", handlerMethod.getBeanType().getName(),
                            "method", handlerMethod.getMethod().getName(),
                            "parameters", handlerMethod.getMethod().getParameterCount()
                        ));
                    }
                }
            }
            
            response.put("success", true);
            response.put("totalMappings", mappings.size());
            response.put("mappings", mappings);
            response.put("message", "Mappings obtenidos exitosamente");
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", "Error al obtener mappings: " + e.getMessage());
        }
        
        return response;
    }

    @GetMapping("/health")
    public Map<String, Object> healthCheck() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            response.put("status", "UP");
            response.put("timestamp", java.time.LocalDateTime.now());
            response.put("application", "AppMartin Backend");
            response.put("version", "1.0.0");
            response.put("security", "DISABLED_FOR_TESTING");
            response.put("database", "CONNECTED");
            
        } catch (Exception e) {
            response.put("status", "DOWN");
            response.put("error", e.getMessage());
        }
        
        return response;
    }

    @GetMapping("/conflicts")
    public Map<String, Object> checkConflicts() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Map<RequestMappingInfo, org.springframework.web.method.HandlerMethod> handlerMethods = 
                requestMappingHandlerMapping.getHandlerMethods();
            
            Map<String, Integer> pathCounts = new HashMap<>();
            Map<String, Object> conflicts = new HashMap<>();
            
            // Contar ocurrencias de cada ruta
            for (RequestMappingInfo mappingInfo : handlerMethods.keySet()) {
                Set<String> patterns = mappingInfo.getPatternsCondition().getPatterns();
                Set<RequestMethod> methods = mappingInfo.getMethodsCondition().getMethods();
                
                for (String pattern : patterns) {
                    for (RequestMethod method : methods) {
                        String key = method.name() + " " + pattern;
                        pathCounts.put(key, pathCounts.getOrDefault(key, 0) + 1);
                    }
                }
            }
            
            // Identificar conflictos
            for (Map.Entry<String, Integer> entry : pathCounts.entrySet()) {
                if (entry.getValue() > 1) {
                    conflicts.put(entry.getKey(), Map.of(
                        "count", entry.getValue(),
                        "status", "CONFLICT"
                    ));
                }
            }
            
            response.put("success", true);
            response.put("totalPaths", pathCounts.size());
            response.put("conflictsFound", conflicts.size());
            response.put("conflicts", conflicts);
            response.put("message", conflicts.isEmpty() ? "No se encontraron conflictos" : "Se encontraron conflictos de rutas");
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", "Error al verificar conflictos: " + e.getMessage());
        }
        
        return response;
    }
}
