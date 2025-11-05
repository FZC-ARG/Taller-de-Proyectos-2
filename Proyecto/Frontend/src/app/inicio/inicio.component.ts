import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AdminService } from '../services/admin.service';
import Swal from 'sweetalert2';


@Component({
  selector: 'app-inicio',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './inicio.component.html',
  styleUrls: ['./inicio.component.css']
})
export class InicioComponent {

  datosAdmin: any;

  alumnos: any[] = []; // <--- array vacío
  docentes: any[] = []; // <--- array vacío

  ngOnInit() {
    this.datosAdmin = JSON.parse(localStorage.getItem('datosAdmin')!);
    console.log(this.datosAdmin);

    if (this.datosAdmin?.idAdmin) {
      this.cargarAlumnos();
      this.cargarDocentes();
    }
  }

  constructor(private router: Router , private adminService: AdminService) {}

  cargarAlumnos() {
    this.adminService.getAlumnos().subscribe({
      next: (data) => {
        this.alumnos = data;
        console.log('Alumnos cargados:', this.alumnos);
      },
      error: (err) => {
        console.error('Error al obtener alumnos:', err);
      }
    });
  }

  cargarDocentes() {
    this.adminService.getDocentes().subscribe({
      next: (data) => {
        this.docentes = data;
        console.log('Docentes cargados:', this.docentes);
      },
      error: (err) => {
        console.error('Error al obtener docentes:', err);
      }
    });
  }

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
