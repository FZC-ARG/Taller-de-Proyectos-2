package com.appmartin.desmartin.service;

import com.appmartin.desmartin.repository.ChatMensajeRepository;
import com.appmartin.desmartin.repository.ChatSesionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Servicio para limpiar automáticamente mensajes y sesiones de chat antiguos
 * según la política de retención de 30 días.
 * 
 * Este servicio se ejecuta automáticamente todos los días a las 2:00 AM
 * y elimina mensajes y sesiones con más de 30 días de antigüedad.
 * 
 * @author Desmartin Team
 * @version 1.0
 */
@Service
public class LimpiezaChatService {
    
    private static final Logger logger = LoggerFactory.getLogger(LimpiezaChatService.class);
    
    @Autowired
    private ChatMensajeRepository chatMensajeRepository;
    
    @Autowired
    private ChatSesionRepository chatSesionRepository;
    
    /**
     * Días de retención configurable (por defecto 30 días)
     * Puede ser configurado en application.properties con:
     * chat.retencion.dias=30
     */
    @Value("${chat.retencion.dias:30}")
    private int diasRetencion;
    
    /**
     * Tarea programada que se ejecuta automáticamente todos los días a las 2:00 AM
     * Cron expression: "0 0 2 * * ?" significa:
     * - 0 segundos
     * - 0 minutos
     * - 2 horas (2 AM)
     * - Cualquier día del mes
     * - Cualquier mes
     * - Cualquier día de la semana
     */
    @Scheduled(cron = "${chat.limpieza.cron:0 0 2 * * ?}")
    @Transactional
    public void limpiarChatAntiguo() {
        logger.info("=== Iniciando limpieza automática de chat ===");
        logger.info("Días de retención configurados: {}", diasRetencion);
        
        LocalDateTime fechaLimite = LocalDateTime.now().minusDays(diasRetencion);
        logger.info("Fecha límite de eliminación: {}", fechaLimite);
        
        try {
            // Primero eliminar mensajes antiguos
            int mensajesEliminados = limpiarMensajesAntiguos(fechaLimite);
            
            // Luego eliminar sesiones antiguas (los mensajes ya fueron eliminados)
            // Nota: Si hay CASCADE en la BD, los mensajes se eliminarán automáticamente
            int sesionesEliminadas = limpiarSesionesAntiguas(fechaLimite);
            
            logger.info("=== Limpieza completada ===");
            logger.info("Mensajes eliminados: {}", mensajesEliminados);
            logger.info("Sesiones eliminadas: {}", sesionesEliminadas);
            
        } catch (Exception e) {
            logger.error("Error durante la limpieza automática de chat", e);
        }
    }
    
    /**
     * Elimina mensajes con fecha anterior a la fecha límite
     * 
     * @param fechaLimite Fecha límite (mensajes anteriores serán eliminados)
     * @return Número de mensajes eliminados
     */
    @Transactional
    public int limpiarMensajesAntiguos(LocalDateTime fechaLimite) {
        logger.info("Eliminando mensajes anteriores a: {}", fechaLimite);
        
        int mensajesEliminados = chatMensajeRepository.deleteByFechaHoraEnvioBefore(fechaLimite);
        
        if (mensajesEliminados > 0) {
            logger.info("Se eliminaron {} mensajes antiguos", mensajesEliminados);
        } else {
            logger.debug("No se encontraron mensajes antiguos para eliminar");
        }
        
        return mensajesEliminados;
    }
    
    /**
     * Elimina sesiones con fecha anterior a la fecha límite
     * 
     * Nota: Los mensajes asociados a estas sesiones deberían eliminarse primero
     * o configurarse CASCADE en la base de datos para eliminación automática.
     * 
     * @param fechaLimite Fecha límite (sesiones anteriores serán eliminadas)
     * @return Número de sesiones eliminadas
     */
    @Transactional
    public int limpiarSesionesAntiguas(LocalDateTime fechaLimite) {
        logger.info("Eliminando sesiones anteriores a: {}", fechaLimite);
        
        int sesionesEliminadas = chatSesionRepository.deleteByFechaCreacionBefore(fechaLimite);
        
        if (sesionesEliminadas > 0) {
            logger.info("Se eliminaron {} sesiones antiguas", sesionesEliminadas);
        } else {
            logger.debug("No se encontraron sesiones antiguas para eliminar");
        }
        
        return sesionesEliminadas;
    }
    
    /**
     * Método manual para ejecutar limpieza (útil para testing o ejecución manual)
     * 
     * @param diasRetencion Días de retención a usar para esta limpieza
     * @return Resultado de la limpieza con estadísticas
     */
    public LimpiezaResultado ejecutarLimpiezaManual(int diasRetencion) {
        logger.info("=== Ejecutando limpieza manual ===");
        logger.info("Días de retención: {}", diasRetencion);
        
        LocalDateTime fechaLimite = LocalDateTime.now().minusDays(diasRetencion);
        
        int mensajesEliminados = limpiarMensajesAntiguos(fechaLimite);
        int sesionesEliminadas = limpiarSesionesAntiguas(fechaLimite);
        
        return new LimpiezaResultado(mensajesEliminados, sesionesEliminadas, fechaLimite);
    }
    
    /**
     * Clase para encapsular el resultado de la limpieza
     */
    public static class LimpiezaResultado {
        private final int mensajesEliminados;
        private final int sesionesEliminadas;
        private final LocalDateTime fechaLimite;
        
        public LimpiezaResultado(int mensajesEliminados, int sesionesEliminadas, LocalDateTime fechaLimite) {
            this.mensajesEliminados = mensajesEliminados;
            this.sesionesEliminadas = sesionesEliminadas;
            this.fechaLimite = fechaLimite;
        }
        
        public int getMensajesEliminados() {
            return mensajesEliminados;
        }
        
        public int getSesionesEliminadas() {
            return sesionesEliminadas;
        }
        
        public LocalDateTime getFechaLimite() {
            return fechaLimite;
        }
        
        @Override
        public String toString() {
            return String.format("LimpiezaResultado{mensajes=%d, sesiones=%d, fechaLimite=%s}", 
                mensajesEliminados, sesionesEliminadas, fechaLimite);
        }
    }
}

