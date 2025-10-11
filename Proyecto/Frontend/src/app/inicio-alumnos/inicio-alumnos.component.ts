import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-inicio-alumnos',
  imports: [FormsModule, CommonModule],
  templateUrl: './inicio-alumnos.component.html',
  styleUrl: './inicio-alumnos.component.css'
})
export class InicioAlumnosComponent {
  seccionActual: 'cursos' | 'test' = 'cursos';

  cursos = [
    'Matemáticas',
    'Comunicación',
    'Ciencia y Tecnología',
    'Historia',
    'Inglés'
  ];

  constructor(private router: Router) {}

  mostrarSeccion(seccion: 'cursos' | 'test') {
    this.seccionActual = seccion;
  }

  irATestGardner() {
    this.router.navigate(['/test-gardner']);
  }

  logout() {
    localStorage.clear();
    this.router.navigate(['/login']);
  }
}
