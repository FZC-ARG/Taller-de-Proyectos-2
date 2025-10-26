package com.appmartin.desmartin.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "resultados_test")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultadoTest {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_resultado")
    private Integer idResultado;
    
    @ManyToOne
    @JoinColumn(name = "id_intento_fk", nullable = false)
    private IntentoTest intentoTest;
    
    @ManyToOne
    @JoinColumn(name = "id_inteligencia_fk", nullable = false)
    private TipoInteligencia tipoInteligencia;
    
    @Column(name = "puntaje_calculado", nullable = false)
    private Float puntajeCalculado;
}

