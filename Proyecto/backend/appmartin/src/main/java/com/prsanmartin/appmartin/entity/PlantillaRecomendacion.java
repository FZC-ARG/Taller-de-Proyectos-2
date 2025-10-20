package com.prsanmartin.appmartin.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Entity
@Table(name = "plantillas_recomendaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlantillaRecomendacion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_plantilla")
    private Integer idPlantilla;
    
    @Column(name = "nombre_plantilla", nullable = false, length = 100)
    private String nombrePlantilla;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_inteligencia", nullable = false)
    private TipoInteligencia tipoInteligencia;
    
    @Column(name = "puntaje_minimo", nullable = false)
    private Integer puntajeMinimo = 0;
    
    @Column(name = "puntaje_maximo", nullable = false)
    private Integer puntajeMaximo = 100;
    
    @Column(name = "contenido_recomendacion", nullable = false, columnDefinition = "TEXT")
    private String contenidoRecomendacion;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_recomendacion", nullable = false)
    private TipoRecomendacion tipoRecomendacion;
    
    @ColumnDefault("true")
    @Column(name = "activo")
    private Boolean activo = true;
    
    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;
    
    @Column(name = "fecha_modificacion")
    private LocalDateTime fechaModificacion;
    
    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
        fechaModificacion = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        fechaModificacion = LocalDateTime.now();
    }
    
    public enum TipoInteligencia {
        musical("musical"),
        logico_matematico("logico_matematico"),
        espacial("espacial"),
        linguistico("linguistico"),
        corporal_cinestesico("corporal_cinestesico"),
        interpersonal("interpersonal"),
        intrapersonal("intrapersonal"),
        naturalista("naturalista");
        
        private final String value;
        
        TipoInteligencia(String value) {
            this.value = value;
        }
        
        public String getValue() {
            return value;
        }
    }
    
    public enum TipoRecomendacion {
        academica("academica"),
        extracurricular("extracurricular"),
        carrera("carrera"),
        personal("personal");
        
        private final String value;
        
        TipoRecomendacion(String value) {
            this.value = value;
        }
        
        public String getValue() {
            return value;
        }
    }
}
