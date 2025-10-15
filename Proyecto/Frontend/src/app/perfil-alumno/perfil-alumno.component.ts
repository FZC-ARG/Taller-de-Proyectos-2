import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router,ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';

@Component({
  selector: 'app-perfil-alumno',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './perfil-alumno.component.html',
  styleUrls: ['./perfil-alumno.component.css']
})
export class PerfilAlumnoComponent {
  constructor (private router: Router , private route: ActivatedRoute, private location: Location) {}
  seccionActual: string = 'perfil';

  alumno!: {
    nombre: string;
    genero: string;
    edad: number;
  };

  ngOnInit() {
    const nombre = this.route.snapshot.queryParams['nombre'] || 'Invitado';
    this.alumno = {
      nombre: nombre,
      genero: 'M',
      edad: 16,
    };
  }


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

  Regresar() {
    this.location.back();
  }

  getFotoPerfil(): string {
    return this.alumno.genero === 'F'
      ? 'https://cdn-icons-png.flaticon.com/512/201/201634.png'
      : 'https://cdn-icons-png.flaticon.com/512/201/201623.png';
  }
}
