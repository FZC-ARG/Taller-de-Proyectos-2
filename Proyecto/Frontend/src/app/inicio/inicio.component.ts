import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import Swal from 'sweetalert2';


@Component({
  selector: 'app-inicio',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './inicio.component.html',
  styleUrls: ['./inicio.component.css']
})
export class InicioComponent {

  constructor(private router: Router) {}
  alumnos: string[] = [
    'Juan Pérez', 'María López', 'Carlos Sánchez',
    'Ana Torres', 'Luis Ramos', 'Valeria Díaz'
  ];

  docentes: string[] = [
    'Prof. José Martínez', 'Prof. Laura Gutiérrez',
    'Prof. Mario Vargas', 'Prof. Carmen Huamán'
  ];

  pruebas: string[] = [
    'Prueba Matemáticas - 1', 'Prueba Comunicación - 2',
    'Prueba Ciencias - 3', 'Prueba Historia - 4'
  ];

  logout() {
    this.router.navigate(['login']);
    Swal.fire({
      title: 'Sesión cerrada',
      text: 'Ahora no tienes acceso a la aplicación',
      icon: 'success',
      confirmButtonText: 'Aceptar',
    });
  }

  actividad() {
    this.router.navigate(['actividad-usuarios']);
  }

  recuperarContrasena() {
    this.router.navigate(['recuperar-contrasena']);
  }
}
