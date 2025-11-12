import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { Location } from '@angular/common';
import { AlumnosService } from '../services/alumnos.service';
import { TestService } from '../services/test.service';
import { NgChartsModule } from 'ng2-charts';
import { ChartConfiguration } from 'chart.js';

@Component({
  selector: 'app-perfil-alumno',
  standalone: true,
  imports: [CommonModule, NgChartsModule],
  templateUrl: './perfil-alumno.component.html',
  styleUrls: ['./perfil-alumno.component.css']
})
export class PerfilAlumnoComponent {
  constructor(
    private router: Router,
    private location: Location,
    private route: ActivatedRoute,
    private alumnosService: AlumnosService,
    private testService: TestService
  ) {}

  seccionActual: string = 'perfil';
  alumno!: { nombre: string; genero: string; edad: number };
  inteligencias: any[] = [];
  inteligenciaMax: any = null;
  cursos: any[] = [];

  // Gráficos
  chartOptions: ChartConfiguration['options'] = {
    responsive: true,
    plugins: { legend: { display: true, position: 'bottom' } }
  };

  barChartData: ChartConfiguration<'bar'>['data'] = { labels: [], datasets: [] };
  lineCharts: { tipo: string; data: ChartConfiguration<'line'>['data'] }[] = [];

  ngOnInit() {
    this.cargarDatosAlumno();
  }

  mostrarSeccion(seccion: string) {
    this.seccionActual = seccion;
  }

  cargarDatosAlumno() {
    const idAlumno = this.route.snapshot.paramMap.get('idAlumno');
    if (idAlumno) {
      this.alumnosService.getAlumnoById(idAlumno).subscribe({
        next: (data) => {
          const hoy = new Date(new Date().toLocaleString('en-US', { timeZone: 'America/Lima' }));
          const fechaNac = new Date(data.fechaNacimiento);
          let edad = hoy.getFullYear() - fechaNac.getFullYear();
          const m = hoy.getMonth() - fechaNac.getMonth();
          if (m < 0 || (m === 0 && hoy.getDate() < fechaNac.getDate())) edad--;

          this.alumno = {
            nombre: `${data.nombre} ${data.apellido}`,
            genero: data.genero || 'M',
            edad
          };

          this.cargarResultadosTest(idAlumno);
          this.cargarCursos(idAlumno);
          this.cargarEvolucionInteligencias(idAlumno);
        },
        error: (err) => console.error('Error al obtener datos del alumno:', err)
      });
    }
  }

  cargarResultadosTest(idAlumno: string) {
    this.testService.getResultados(idAlumno).subscribe({
      next: (data) => {
        const tipos: Record<number, string> = {
          1: 'Lingüística',
          2: 'Lógico-Matemática',
          3: 'Espacial',
          4: 'Corporal-Kinestésica',
          5: 'Musical',
          6: 'Interpersonal',
          7: 'Intrapersonal',
          8: 'Naturalista'
        };

        this.inteligencias = data.resultados.map((r: any) => ({
          tipo: tipos[r.idInteligencia as number] || 'Desconocida',
          nivel: Math.round(r.puntajeCalculado * 10)
        }));

        this.inteligenciaMax = this.inteligencias.reduce((a, b) => (a.nivel > b.nivel ? a : b));

        const labels = this.inteligencias.map(i => i.tipo);
        const valores = this.inteligencias.map(i => i.nivel);

        this.barChartData = {
          labels,
          datasets: [{ label: 'Nivel actual (%)', data: valores, backgroundColor: '#4F46E5' }]
        };
      },
      error: (err) => console.error('Error al obtener resultados del test:', err)
    });
  }

  cargarEvolucionInteligencias(idAlumno: string) {
    this.testService.getIntentos(idAlumno).subscribe({
      next: (intentos) => {
        if (!intentos || intentos.length === 0) return;
        intentos.sort((a: any, b: any) => a.numeroIntento - b.numeroIntento);

        // Obtener los nombres de inteligencias desde el primer intento
        const inteligencias = intentos[0].resultados.map((r: any) => r.nombreInteligencia);
        const lineChartsTemp: any[] = [];

        inteligencias.forEach((nombre: string) => {
          const valores = intentos.map((i: any) => {
            const resultado = i.resultados.find((r: any) => r.nombreInteligencia === nombre);
            return resultado ? Math.round(resultado.puntajeCalculado * 10) : 0;
          });

          const labels = intentos.map((i: any) => `Intento ${i.numeroIntento}`);

          lineChartsTemp.push({
            tipo: nombre,
            data: {
              labels,
              datasets: [
                {
                  label: nombre,
                  data: valores,
                  fill: false,
                  borderColor: '#22C55E',
                  tension: 0.3
                }
              ]
            }
          });
        });

        this.lineCharts = lineChartsTemp;
      },
      error: (err) => console.error('Error al obtener evolución de inteligencias:', err)
    });
  }

  cargarCursos(idAlumno: string): void {
    this.alumnosService.getCursosAlumno(idAlumno).subscribe({
      next: (data) => (this.cursos = data),
      error: (err) => console.error('Error al obtener cursos:', err)
    });
  }

  regresar() {
    this.location.back();
  }

  getFotoPerfil(): string {
    return this.alumno?.genero === 'F'
      ? 'https://cdn-icons-png.flaticon.com/512/201/201634.png'
      : 'https://cdn-icons-png.flaticon.com/512/201/201623.png';
  }
}
