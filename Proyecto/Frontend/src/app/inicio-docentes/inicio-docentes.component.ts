import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-inicio-docentes',
  imports: [FormsModule, CommonModule],
  templateUrl: './inicio-docentes.component.html',
  styleUrl: './inicio-docentes.component.css'
})
export class InicioDocentesComponent {

  seccionActual: 'alumnos' | 'test' = 'alumnos';

  alumnos = [
    'Juan',
    'Pedro',
    'Luis',
    'Enrique',
    'Carlos',
    'Diego',
    'Fernando',
    'Gabriel',
    'Miguel',
    'Jose',
    'Maria',
    'Ana',
    'María',
    'Laura',
    'Sofía',
    'Pablo',
    'Javier',
    'Daniel',
    'Ángel',
    'Lucas',
  ];

  constructor(private router: Router) {}

  mostrarSeccion(seccion: 'alumnos' | 'test') {
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

