import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import Swal from 'sweetalert2';
import { AlumnosService } from '../services/alumnos.service';


@Component({
  selector: 'app-inicio-alumnos',
  imports: [FormsModule, CommonModule],
  templateUrl: './inicio-alumnos.component.html',
  styleUrl: './inicio-alumnos.component.css'
})
export class InicioAlumnosComponent {
  seccionActual: 'cursos' | 'test' = 'cursos';

  datosAlumno: any;

  cursos : any[] = []; // <--- array vacío

  constructor(private router: Router , private alumnosService: AlumnosService) {}

  ngOnInit() {
    this.datosAlumno = JSON.parse(localStorage.getItem('datosAlumno')!);
    console.log(this.datosAlumno);

    if (this.datosAlumno?.idAlumno) {
      this.cargarCursos(this.datosAlumno.idAlumno);
    }
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

  mostrarSeccion(seccion: 'cursos' | 'test') {
    this.seccionActual = seccion;
  }

  irATestGardner() {
    this.router.navigate(['/test-gardner']);
  }

  logout() {
    localStorage.clear();
    this.router.navigate(['/login']);

    Swal.fire({
      title: 'Sesión cerrada',
      text: 'Ahora no tienes acceso a la aplicación',
      icon: 'success',
      confirmButtonText: 'Aceptar',
    });
  }
}
