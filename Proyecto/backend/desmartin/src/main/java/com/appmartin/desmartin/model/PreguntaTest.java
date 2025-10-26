package com.appmartin.desmartin.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "preguntas_test")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PreguntaTest {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pregunta")
    private Integer idPregunta;
    
    @ManyToOne
    @JoinColumn(name = "id_inteligencia_fk", nullable = false)
    private TipoInteligencia tipoInteligencia;
    
    @Column(name = "texto_pregunta", nullable = false, columnDefinition = "TEXT")
    private String textoPregunta;
}

