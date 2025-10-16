import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import Swal from 'sweetalert2';
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
    'Juan', 'Pedro', 'Luis', 'Enrique', 'Carlos', 'Diego', 'Fernando',
    'Gabriel', 'Miguel', 'Jose', 'Maria', 'Ana', 'María', 'Laura',
    'Sofía', 'Pablo', 'Javier', 'Daniel', 'Ángel', 'Lucas',
  ];

  constructor(private router: Router) {}

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

    Swal.fire({
      title: 'Sesión cerrada',
      text: 'Ahora no tienes acceso a la aplicación',
      icon: 'success',
      confirmButtonText: 'Aceptar',
    });
  }
}
