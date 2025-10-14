import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'app-perfil-alumno',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './perfil-alumno.component.html',
  styleUrls: ['./perfil-alumno.component.css']
})
export class PerfilAlumnoComponent {
  constructor (private router: Router) { }
  seccionActual: string = 'perfil';

  alumno = {
    nombre: 'Juan Pérez',
    genero: 'M', // 'M' o 'F'
    edad: 16,
  };

  inteligencias = [
    { tipo: 'Lingüística', nivel: 85 },
    { tipo: 'Lógico-Matemática', nivel: 90 },
    { tipo: 'Espacial', nivel: 70 },
    { tipo: 'Corporal-Kinestésica', nivel: 60 },
    { tipo: 'Musical', nivel: 50 },
    { tipo: 'Interpersonal', nivel: 80 },
    { tipo: 'Intrapersonal', nivel: 75 },
    { tipo: 'Naturalista', nivel: 65 }
  ];

  cursos = ['Matemática I', 'Programación', 'Física', 'Inglés', 'Estadística'];

  mostrarSeccion(seccion: string) {
    this.seccionActual = seccion;
  }

  logout() {
    console.log('Sesión cerrada');
    this.router.navigate(['/login']);

  }

  getFotoPerfil(): string {
    return this.alumno.genero === 'F'
      ? 'https://cdn-icons-png.flaticon.com/512/201/201634.png'
      : 'https://cdn-icons-png.flaticon.com/512/201/201623.png';
  }
}
