package com.appmartin.desmartin.ia;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Servicio centralizado para gestionar la personalidad de la IA conversacional.
 *
 * <p>Permite definir perfiles de personalidad reutilizables que pueden ser
 * aplicados al mensaje de sistema enviado al modelo de IA. El perfil activo
 * puede cambiarse en tiempo de ejecución o configurarse mediante la propiedad
 * {@code ia.personality.default} (por ejemplo en {@code application.properties}).</p>
 *
 * <p>Ejemplo de configuración en {@code application.properties}:</p>
 * <pre>{@code
 * ia.personality.default=yae_miko
 * }</pre>
 *
 * <p>Perfiles incluidos:</p>
 * <ul>
 *     <li><strong>educational_default</strong>: tono profesional y formativo.</li>
 *     <li><strong>yae_miko</strong>: inspirado en el personaje Yae Miko de Genshin Impact.</li>
 * </ul>
 *
 * Nuevos perfiles pueden añadirse usando {@link #registerPersonality(IAPersonalityProfile)}.
 */
@Service
public class IAPersonalityService {

    private static final Logger logger = LoggerFactory.getLogger(IAPersonalityService.class);

    private final Map<String, IAPersonalityProfile> personas = new ConcurrentHashMap<>();

    @Value("${ia.personality.default:educational_default}")
    private String defaultPersonalityKey;

    private volatile String activePersonalityKey;

    public IAPersonalityService() {
        bootstrapDefaultPersonalities();
    }

    @PostConstruct
    public void init() {
        if (!personas.containsKey(defaultPersonalityKey)) {
            logger.warn("La personalidad por defecto '{}' no está registrada. Se utilizará 'educational_default'.", defaultPersonalityKey);
            defaultPersonalityKey = "educational_default";
        }
        activePersonalityKey = defaultPersonalityKey;
        logger.info("Personalidad IA activa: {}", activePersonalityKey);
    }

    /**
     * Obtiene el perfil actualmente activo.
     *
     * @return perfil activo (nunca {@code null})
     */
    public IAPersonalityProfile getActivePersonality() {
        return Optional.ofNullable(personas.get(activePersonalityKey))
            .orElseGet(() -> personas.get("educational_default"));
    }

    /**
     * Establece la personalidad activa mediante su clave.
     *
     * @param key clave del perfil registrado
     * @return {@code true} si la clave existe y se activó, {@code false} en caso contrario
     */
    public boolean setActivePersonality(String key) {
        if (key == null || !personas.containsKey(key)) {
            logger.warn("No se encontró la personalidad '{}'. La personalidad activa no cambió.", key);
            return false;
        }
        this.activePersonalityKey = key;
        logger.info("Personalidad IA cambiada a {}", key);
        return true;
    }

    /**
     * Registra una nueva personalidad. Si la clave ya existe, se sobrescribe.
     *
     * @param profile nuevo perfil
     */
    public void registerPersonality(IAPersonalityProfile profile) {
        if (profile == null || profile.key() == null) {
            throw new IllegalArgumentException("El perfil y su clave no pueden ser nulos.");
        }
        personas.put(profile.key(), profile);
        logger.info("Personalidad '{}' registrada/actualizada.", profile.key());
    }

    /**
     * Devuelve las personalidades disponibles.
     *
     * @return mapa inmutable de perfiles registrados
     */
    public Map<String, IAPersonalityProfile> getRegisteredPersonalities() {
        return Collections.unmodifiableMap(new HashMap<>(personas));
    }

    /**
     * Devuelve la descripción de personalidad para incluir en el prompt del sistema.
     *
     * @return cadena lista para concatenar al mensaje del sistema
     */
    public String buildPersonalityPrompt() {
        IAPersonalityProfile profile = getActivePersonality();
        return """
            PERSONALIDAD DE LA IA (%s):
            ----------------------------------------
            %s
            """.formatted(profile.displayName(), profile.prompt());
    }

    private void bootstrapDefaultPersonalities() {
        String corePedagogicalPrompt = """
            [ROL: IA EDUCATIVA PERSONALIZADA BASADA EN GARDNER]
    
            Tu función principal es analizar el perfil cognitivo de cada estudiante, comprendido a través de la teoría de las inteligencias múltiples de Howard Gardner. 
            Usa esos datos para generar recomendaciones precisas y útiles que ayuden al profesor a planificar estrategias de enseñanza personalizadas o, si corresponde, dar consejos de aprendizaje al propio estudiante.
    
            INSTRUCCIONES PRINCIPALES:
            1. Analiza cuidadosamente el JSON que contiene el perfil del estudiante, el cual incluye:
               - Nombre del estudiante
               - Edad o grado educativo
               - Inteligencias predominantes (ejemplo: lingüística, lógico-matemática, espacial, corporal-kinestésica, interpersonal, intrapersonal, musical, naturalista)
               - Nivel porcentual o ponderación de cada tipo de inteligencia
               - Observaciones o comentarios del docente (opcional)
    
            2. Según los valores predominantes, genera RECOMENDACIONES PERSONALIZADAS enfocadas en:
               - Estrategias de enseñanza o actividades ideales para su tipo de inteligencia.
               - Consejos para potenciar inteligencias menos desarrolladas.
               - Recursos o métodos sugeridos (juegos, herramientas, dinámicas, materiales digitales, etc.).
               - Sugerencias de planificación de clase adaptadas al contexto (si el receptor es el profesor).
    
            3. Adapta el tono y estilo según la personalidad activa.
               - “educational_default”: profesional, didáctico, empático.
               - “yae_miko”: encantadora, astuta, elegante, con un toque juguetón, pero rigurosa pedagógicamente.
    
            4. Formato de salida esperado:
               🧩 Inteligencias predominantes:
               (lista breve con porcentajes)
               
               🎓 Recomendaciones personalizadas:
               (análisis breve con consejos concretos)
    
               📘 Estrategias sugeridas:
               - Actividad 1: ...
               - Actividad 2: ...
               - Recurso adicional: ...
    
            Si el perfil JSON está incompleto, solicita la información faltante antes de emitir recomendaciones.
            """;
    
        personas.put("educational_default", new IAPersonalityProfile(
            "educational_default",
            "Mentor Pedagógico",
            """
                Mantén un tono profesional, empático y centrado en soluciones pedagógicas.
                Explica recomendaciones con fundamento y evita conjeturas sin datos.
                Prioriza la claridad y la utilidad práctica.
                
            """ + corePedagogicalPrompt
        ));
    
        personas.put("yae_miko", new IAPersonalityProfile(
            "yae_miko",
            "Yae Miko (Genshin Impact)",
            """
                Adopta la personalidad de Yae Miko: elegante, astuta y ligeramente pícara.
                Usa un tono juguetón pero respetuoso y amigable.
                Aun con su encanto travieso, ofrece consejos pedagógicos precisos y bien fundamentados.
                
            """ + corePedagogicalPrompt
        ));
    }
    

    /**
     * Registro inmutable que describe una personalidad de IA.
     *
     * @param key         identificador único (case-sensitive)
     * @param displayName nombre descriptivo para mostrar
     * @param prompt      instrucciones específicas para el modelo
     */
    public record IAPersonalityProfile(String key, String displayName, String prompt) {
    }
}

