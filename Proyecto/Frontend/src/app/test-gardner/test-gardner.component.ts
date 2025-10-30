import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-test-gardner',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './test-gardner.component.html',
  styleUrls: ['./test-gardner.component.css']
})
export class TestGardnerComponent {
  preguntasPorPagina = 5;
  paginaActual = 1;

  tiempoInicio: number = 0;
  tiempoTranscurrido: number = 0;
  intervalo: any;
  minutos: number = 0;
  segundos: number = 0;

  preguntas = [
    { id: 1, texto: 'Me gusta resolver problemas matemáticos o lógicos.', respuesta: null },
    { id: 2, texto: 'Disfruto participar en actividades musicales.', respuesta: null },
    { id: 3, texto: 'Prefiero aprender a través de imágenes, diagramas o mapas mentales.', respuesta: null },
    { id: 4, texto: 'Puedo expresar mis ideas fácilmente con palabras.', respuesta: null },
    { id: 5, texto: 'Me resulta fácil trabajar en grupo.', respuesta: null },
    { id: 6, texto: 'Me gusta observar la naturaleza y aprender de ella.', respuesta: null },
    { id: 7, texto: 'Soy consciente de mis emociones y cómo influyen en mí.', respuesta: null },
    { id: 8, texto: 'Me gusta moverme y aprender haciendo actividades físicas.', respuesta: null },
    { id: 9, texto: 'Disfruto resolver acertijos y desafíos.', respuesta: null },
    { id: 10, texto: 'Me inspiro al escuchar música.', respuesta: null },
    { id: 11, texto: 'Me gusta leer libros.', respuesta: null },
    { id: 12, texto: 'Me gusta jugar videojuegos.', respuesta: null },
    { id: 13, texto: 'Me gusta jugar a los juegos de mesa.', respuesta: null },
    { id: 14, texto: 'Me gusta jugar a los juegos de mesa.', respuesta: null },
    { id: 15, texto: 'Me gusta jugar a los juegos de mesa.', respuesta: null },
    { id: 16, texto: 'Me gusta jugar a los juegos de mesa.', respuesta: null },
    { id: 17, texto: 'Me gusta jugar a los juegos de mesa.', respuesta: null },
    { id: 18, texto: 'Me gusta jugar a los juegos de mesa.', respuesta: null },
    { id: 19, texto: 'Me gusta jugar a los juegos de mesa.', respuesta: null },
    { id: 20, texto: 'Me gusta jugar a los juegos de mesa.', respuesta: null }, 
    { id: 21, texto: 'Me gusta jugar a los juegos de mesa.', respuesta: null },
  ];

  constructor(private router: Router) {}

  ngOnInit() {
    this.iniciarTemporizador();
  }
  
  iniciarTemporizador() {
    this.tiempoInicio = Date.now();
    this.intervalo = setInterval(() => {
      const ahora = Date.now();
      this.tiempoTranscurrido = Math.floor((ahora - this.tiempoInicio) / 1000);
      this.minutos = Math.floor(this.tiempoTranscurrido / 60);
      this.segundos = this.tiempoTranscurrido % 60;
    }, 1000);
  }
  
  detenerTemporizador() {
    clearInterval(this.intervalo);
  }
  get totalPaginas() {
    return Math.ceil(this.preguntas.length / this.preguntasPorPagina);
  }

  get inicioIndice() {
    return (this.paginaActual - 1) * this.preguntasPorPagina + 1;
  }

  get preguntasPaginadas() {
    const inicio = (this.paginaActual - 1) * this.preguntasPorPagina;
    return this.preguntas.slice(inicio, inicio + this.preguntasPorPagina);
  }

  siguientePagina() {
    if (this.paginaActual < this.totalPaginas) this.paginaActual++;
  }

  anteriorPagina() {
    if (this.paginaActual > 1) this.paginaActual--;
  }

  irAPagina(id: number) {
    this.paginaActual = Math.ceil(id / this.preguntasPorPagina);
  }

  onSubmit() {
    const incompletas = this.preguntas.filter(p => p.respuesta === null);
    if (incompletas.length > 0) {
      Swal.fire({
        icon: 'warning',
        title: 'Faltan respuestas',
        text: 'Por favor responde todas las preguntas antes de enviar.',
      });
      return;
    }
    this.detenerTemporizador();
    const minutos = this.minutos;
    const segundos = this.segundos;

    Swal.fire({
      icon: 'success',
      title: '¡Test completado!',
      text: `Completaste el test en ${minutos} minuto(s) y ${segundos} segundo(s).`,
      showConfirmButton: true
    }).then(() => {
      this.router.navigate(['/inicio-alumnos']);
    });

    console.log('Respuestas del test:', this.preguntas);

    
  }
}
