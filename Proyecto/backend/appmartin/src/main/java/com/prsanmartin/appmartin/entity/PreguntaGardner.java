package com.prsanmartin.appmartin.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Entity
@Table(name = "preguntas_gardner")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PreguntaGardner {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pregunta")
    private Integer idPregunta;
    
    @Column(name = "texto_pregunta", columnDefinition = "TEXT", nullable = false)
    private String textoPregunta;
    
    @Column(name = "opcion_a", length = 500, nullable = false)
    private String opcionA;
    
    @Column(name = "opcion_b", length = 500, nullable = false)
    private String opcionB;
    
    @Column(name = "opcion_c", length = 500, nullable = false)
    private String opcionC;
    
    @Column(name = "opcion_d", length = 500, nullable = false)
    private String opcionD;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_inteligencia", nullable = false)
    private TipoInteligencia tipoInteligencia;
    
    @Column(name = "orden_secuencia", nullable = false)
    private Integer ordenSecuencia;
    
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
}
