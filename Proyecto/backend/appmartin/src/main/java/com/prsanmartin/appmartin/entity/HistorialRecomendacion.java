package com.prsanmartin.appmartin.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Entity
@Table(name = "historial_recomendaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistorialRecomendacion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_historial")
    private Integer idHistorial;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_alumno", nullable = false)
    private Alumno alumno;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_test", nullable = false)
    private TestGardner test;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_plantilla", nullable = false)
    private PlantillaRecomendacion plantilla;
    
    @Column(name = "inteligencia_predominante", nullable = false, length = 100)
    private String inteligenciaPredominante;
    
    @Column(name = "puntaje_inteligencia", nullable = false)
    private Integer puntajeInteligencia;
    
    @Column(name = "recomendacion_generada", nullable = false, columnDefinition = "TEXT")
    private String recomendacionGenerada;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_recomendacion", nullable = false)
    private TipoRecomendacion tipoRecomendacion;
    
    @Column(name = "fecha_generacion")
    private LocalDateTime fechaGeneracion;
    
    @Column(name = "fecha_aplicacion")
    private LocalDateTime fechaAplicacion;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    @ColumnDefault("'generada'")
    private EstadoRecomendacion estado = EstadoRecomendacion.generada;
    
    @Column(name = "notas", columnDefinition = "TEXT")
    private String notas;
    
    @PrePersist
    protected void onCreate() {
        fechaGeneracion = LocalDateTime.now();
        estado = EstadoRecomendacion.generada;
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
    
    public enum EstadoRecomendacion {
        generada("generada"),
        aplicada("aplicada"),
        descartada("descartada");
        
        private final String value;
        
        EstadoRecomendacion(String value) {
            this.value = value;
        }
        
        public String getValue() {
            return value;
        }
    }
}
