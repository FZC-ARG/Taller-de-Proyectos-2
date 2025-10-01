package com.prsanmartin.appmartin.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.math.BigDecimal;

@Entity
@Table(name = "TestsGardner")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestGardner {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IdTest")
    private Integer idTest;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "IdAlumno", nullable = false)
    private Alumno alumno;
    
    @Column(name = "Respuestas", columnDefinition = "TEXT")
    private String respuestas;
    
    @Column(name = "Puntajes", columnDefinition = "TEXT")
    private String puntajes;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_guardado")
    @ColumnDefault("'BORRADOR'")
    private EstadoGuardado estadoGuardado = EstadoGuardado.BORRADOR;
    
    @Column(name = "version_guardado")
    @ColumnDefault("1")
    private Integer versionGuardado = 1;
    
    @Column(name = "client_request_id", length = 255)
    private String clientRequestId;
    
    @Column(name = "inteligencia_predominante", length = 100)
    private String inteligenciaPredominante;
    
    @Column(name = "puntaje_total", precision = 5, scale = 2)
    private BigDecimal puntajeTotal;
    
    @Column(name = "tiempo_inicio")
    private LocalDateTime tiempoInicio;
    
    @Column(name = "tiempo_fin")
    private LocalDateTime tiempoFin;
    
    @Column(name = "modificado_por", length = 255)
    private String modificadoPor;
    
    @Column(name = "notas", columnDefinition = "TEXT")
    private String notas;
    
    @Column(name = "FechaAplicacion")
    private LocalDateTime fechaAplicacion;
    
    @PrePersist
    protected void onCreate() {
        fechaAplicacion = LocalDateTime.now();
        tiempoInicio = LocalDateTime.now();
        estadoGuardado = EstadoGuardado.BORRADOR;
        versionGuardado = 1;
    }
    
    public enum EstadoGuardado {
        BORRADOR("BORRADOR"),
        FINAL("FINAL"),
        CALCULADO("CALCULADO");
        
        private final String value;
        
        EstadoGuardado(String value) {
            this.value = value;
        }
        
        public String getValue() {
            return value;
        }
    }
}
