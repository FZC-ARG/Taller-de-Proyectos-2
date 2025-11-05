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
  imports: [CommonModule , NgChartsModule],
  templateUrl: './perfil-alumno.component.html',
  styleUrls: ['./perfil-alumno.component.css']
})
export class PerfilAlumnoComponent {
  constructor (
    private router: Router, 
    private location: Location, 
    private route:ActivatedRoute, 
    private alumnosService: AlumnosService,
    private testService: TestService) {}
  seccionActual: string = 'perfil';
  alumno!: { nombre: string; genero: string; edad: number; };
  inteligencias: any[] = [];
  inteligenciaMax: any = null;

  // Configuración de gráficos
  chartOptions: ChartConfiguration['options'] = {
    responsive: true,
    plugins: { legend: { display: true, position: 'bottom' } }
  };

  barChartData: ChartConfiguration<'bar'>['data'] = { labels: [], datasets: [] };
  pieChartData: ChartConfiguration<'pie'>['data'] = { labels: [], datasets: [] };
  doughnutChartData: ChartConfiguration<'doughnut'>['data'] = { labels: [], datasets: [] };
  radarChartData: ChartConfiguration<'radar'>['data'] = { labels: [], datasets: [] };

  ngOnInit() {
    this.cargarDatosAlumno();
  }


  cursos : any[] = []; // <--- array vacío
  mostrarSeccion(seccion: string) {
    this.seccionActual = seccion;
  }


  cargarDatosAlumno() {
    const idAlumno = this.route.snapshot.paramMap.get('idAlumno');
    if (idAlumno) {
      this.alumnosService.getAlumnoById(idAlumno).subscribe({
        next: (data) => {
          // Calcular edad
          const hoy = new Date(new Date().toLocaleString('en-US', { timeZone: 'America/Lima' }));
          const fechaNac = new Date(data.fechaNacimiento);
          let edad = hoy.getFullYear() - fechaNac.getFullYear();
          const m = hoy.getMonth() - fechaNac.getMonth();
          if (m < 0 || (m === 0 && hoy.getDate() < fechaNac.getDate())) {
            edad--;
          }

          this.alumno = {
            nombre: `${data.nombre} ${data.apellido}`,
            genero: data.genero || 'M', // valor por defecto si no viene
            edad
          };

          console.log('Datos del alumno cargados:', this.alumno);
          this.cargarResultadosTest(idAlumno);
          this.cargarCursos(idAlumno);
        },
        error: (err) => {
          console.error('Error al obtener datos del alumno:', err);
        }
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

        // Detectar inteligencia predominante
        this.inteligenciaMax = this.inteligencias.reduce((a, b) => a.nivel > b.nivel ? a : b);

        // Preparar datos para los gráficos
        const labels = this.inteligencias.map(i => i.tipo);
        const valores = this.inteligencias.map(i => i.nivel);

        this.barChartData = {
          labels,
          datasets: [{ label: 'Nivel (%)', data: valores }]
        };
        this.pieChartData = { labels, datasets: [{ data: valores }] };
        this.doughnutChartData = { labels, datasets: [{ data: valores }] };
        this.radarChartData = { labels, datasets: [{ label: 'Perfil de Inteligencias', data: valores }] };

        console.log('Resultados del test cargados:', this.inteligencias);
      },
      error: (err) => console.error('Error al obtener resultados del test:', err)
    });
  }

  cargarCursos(idAlumno: string): void {
    this.alumnosService.getCursosAlumno(idAlumno).subscribe({
      next: (data) => {
        this.cursos = data;
        console.log('Cursos cargados:', this.cursos);
      },
      error: (err) => {
        console.error('Error al obtener cursos:', err);
      }
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
