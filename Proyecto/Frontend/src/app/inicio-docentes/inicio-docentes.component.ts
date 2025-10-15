import { Component } from '@angular/core';
import { Router,ActivatedRoute } from '@angular/router';
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
  modalAbierto = false;

  alumnos = [
    { id: '1', nombre: 'Juan' },
    { id: '2', nombre: 'Pedro' },
    { id: '3', nombre: 'Luis' },
    { id: '4', nombre: 'Enrique' },
    { id: '5', nombre: 'Carlos' },
    { id: '6', nombre: 'Diego' },
    { id: '7', nombre: 'Fernando' },
    { id: '8', nombre: 'Gabriel' },
    { id: '9', nombre: 'Miguel' },
    { id: '10', nombre: 'Jose' },
    { id: '11', nombre: 'Maria' },
    { id: '12', nombre: 'Ana' },
    { id: '13', nombre: 'María' },
    { id: '14', nombre: 'Laura' },
    { id: '15', nombre: 'Sofía' },
  ];

  constructor(private router: Router ,private route: ActivatedRoute) {}

  mostrarSeccion(seccion: 'alumnos' | 'test') {
    this.seccionActual = seccion;
  }

  irATestGardner() {
    this.router.navigate(['/test-gardner']);
  }

  abrirModal() {
    this.modalAbierto = true;
  }

  cerrarModal() {
    this.modalAbierto = false;
  }

  abrirEnlace() {
    window.open('https://utecno.wordpress.com/wp-content/uploads/2014/07/howard_gardner_-_estructuras_de_la_mente.pdf', '_blank'); // Cambia este enlace
  }

  logout() {
    localStorage.clear();
    this.router.navigate(['/login']);
  }
  mostrarPerfil(id: string , nombre: string) {
    this.router.navigate(['/perfil-alumno'], { queryParams: { id: id, nombre: nombre } });
  }
}
