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

    // 🔹 Obtener idAlumno desde localStorage
    const datosAlumno = JSON.parse(localStorage.getItem('datosAlumno') || '{}');
    const idAlumno = datosAlumno.idAlumno;

    // 🔹 Agrupar por idInteligencia
    const resultadosMap = new Map<number, number[]>();

    this.preguntas.forEach(p => {
      if (!resultadosMap.has(p.idInteligencia)) {
        resultadosMap.set(p.idInteligencia, []);
      }
      resultadosMap.get(p.idInteligencia)!.push(Number(p.respuesta));
    });

    // 🔹 Calcular promedios
    const resultados = Array.from(resultadosMap.entries()).map(([idInteligencia, respuestas]) => {
      const suma = respuestas.reduce((acc, val) => acc + val, 0);
      const promedio = suma / 10; // cada inteligencia tiene 10 preguntas
      return {
        idInteligencia,
        puntajeCalculado: Number(promedio.toFixed(2))
      };
    });

    // 🔹 Crear objeto final
    const dataEnviar = { idAlumno, resultados };

    console.log('📤 Datos a enviar:', dataEnviar);

    this.testService.enviarResultados(dataEnviar).subscribe({
      next: (resp) => {
        console.log('✅ Respuesta del backend:', resp);

        Swal.fire({
          icon: 'success',
          title: '¡Resultados guardados correctamente!',
          html: `Completaste el test en <b>${this.minutos} minuto(s)</b> y <b>${this.segundos} segundo(s)</b>.`
        }).then(() => {
          localStorage.removeItem('tiempoTestGardner');
          localStorage.removeItem('respuestasTestGardner');
          this.router.navigate(['/inicio-alumnos']);
        });
      },
      error: (err) => {
        console.error('⚠️ Error al guardar:', err);

        // Si el backend guardó pero devolvió error vacío o sin cuerpo
        if (err.status === 200 || err.status === 201 || err.statusText === 'OK') {
          Swal.fire({
            icon: 'success',
            title: '¡Test completado!',
            text: `Los resultados se guardaron correctamente.`,
          }).then(() => {
            localStorage.removeItem('tiempoTestGardner');
            localStorage.removeItem('respuestasTestGardner');
            localStorage.removeItem('paginaActualTestGardner');
            this.router.navigate(['/inicio-alumnos']);
          });
        } else {
          Swal.fire({
            icon: 'error',
            title: 'Error al guardar',
            text: 'Los resultados se guardaron parcialmente o hubo un error en la respuesta del servidor.'
          });
        }
      }
    });
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
