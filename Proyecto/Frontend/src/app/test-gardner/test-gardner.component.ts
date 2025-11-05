import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import Swal from 'sweetalert2';
import { TestService } from '../services/test.service';

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

  preguntas: any[] = [];

  constructor(
    private router: Router,
    private testService: TestService
  ) {}

  ngOnInit() {
    this.cargarPreguntas();
    this.iniciarTemporizador();
  }

  cargarPreguntas() {
    this.testService.getPreguntas().subscribe({
      next: (data) => {
        this.preguntas = data.map((p:any) => ({
          idPregunta: p.idPregunta,
          textoPregunta: p.textoPregunta,
          idInteligencia: p.idInteligencia,
          nombreInteligencia: p.nombreInteligencia,
          respuesta: null

          
        }));
        this.cargarRespuestasGuardadas();
        const paginaGuardada = localStorage.getItem('paginaActualTestGardner');
        if (paginaGuardada) {
          this.paginaActual = Number(paginaGuardada);
        }

      },
      error: () => {
        Swal.fire({
          icon: 'error',
          title: 'Error',
          text: 'No se pudieron cargar las preguntas.'
        });
      }
    });
  }

  iniciarTemporizador() {
    // ✅ Recuperar tiempo previo si existe
    const guardado = localStorage.getItem('tiempoTestGardner');

    if (guardado) {
      const tiempoPrevio = JSON.parse(guardado);
      this.tiempoInicio = tiempoPrevio.tiempoInicio;
    } else {
      // ✅ Primera vez
      this.tiempoInicio = Date.now();
      localStorage.setItem('tiempoTestGardner', JSON.stringify({
        tiempoInicio: this.tiempoInicio
      }));
    }

    this.intervalo = setInterval(() => {
      const ahora = Date.now();
      this.tiempoTranscurrido = Math.floor((ahora - this.tiempoInicio) / 1000);

      this.minutos = Math.floor(this.tiempoTranscurrido / 60);
      this.segundos = this.tiempoTranscurrido % 60;

      // ✅ Guardar siempre
      localStorage.setItem('tiempoTestGardner', JSON.stringify({
        tiempoInicio: this.tiempoInicio
      }));
    }, 1000);
  }

  detenerTemporizador() {
    clearInterval(this.intervalo);
  }

  get totalPaginas() {
    return Math.ceil(this.preguntas.length / this.preguntasPorPagina);
  }

  get preguntasPaginadas() {
    const inicio = (this.paginaActual - 1) * this.preguntasPorPagina;
    return this.preguntas.slice(inicio, inicio + this.preguntasPorPagina);
  }

  siguientePagina() {
    if (this.paginaActual < this.totalPaginas) {
      this.paginaActual++;
      localStorage.setItem('paginaActualTestGardner', String(this.paginaActual));
    }
  }

  anteriorPagina() {
    if (this.paginaActual > 1) {
      this.paginaActual--;
      localStorage.setItem('paginaActualTestGardner', String(this.paginaActual));
    }
  }

  // ✅ Ahora usa idPregunta
  irAPaginaPorId(idPregunta: number) {
    const index = this.preguntas.findIndex(p => p.idPregunta === idPregunta);
    if (index !== -1) {
      this.paginaActual = Math.floor(index / this.preguntasPorPagina) + 1;
      localStorage.setItem('paginaActualTestGardner', String(this.paginaActual));
    }
  }

  onSubmit() {
    const incompletas = this.preguntas.filter(p => p.respuesta === null);

    if (incompletas.length > 0) {
      Swal.fire({
        icon: 'warning',
        title: 'Faltan respuestas',
        text: 'Por favor responde todas las preguntas antes de enviar.'
      });
      return;
    }

    this.detenerTemporizador();

    Swal.fire({
      icon: 'success',
      title: '¡Test completado!',
      text: `Completaste el test en ${this.minutos} minuto(s) y ${this.segundos} segundo(s).`
    }).then(() => {
      localStorage.removeItem('tiempoTestGardner');
      localStorage.removeItem('respuestasTestGardner');
      this.router.navigate(['/inicio-alumnos']);
    });

    console.log('Respuestas del test:', this.preguntas);
  }

  guardarRespuestas() {
    localStorage.setItem('respuestasTestGardner', JSON.stringify(this.preguntas));
  }

  cargarRespuestasGuardadas() {
    const dataGuardada = localStorage.getItem('respuestasTestGardner');

    if (dataGuardada) {
      const respuestasPrevias = JSON.parse(dataGuardada);

      // Restauramos solo las respuestas
      this.preguntas = this.preguntas.map(p => {
        const guardada = respuestasPrevias.find((x:any) => x.idPregunta === p.idPregunta);
        return guardada ? { ...p, respuesta: guardada.respuesta } : p;
      });
    }
  }

}
