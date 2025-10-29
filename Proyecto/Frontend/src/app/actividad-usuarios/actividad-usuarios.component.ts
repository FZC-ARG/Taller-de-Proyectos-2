import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-actividad-usuarios',
  standalone: true,
  templateUrl: './actividad-usuarios.component.html',
  imports: [CommonModule],
  styleUrl: './actividad-usuarios.component.css'
})
export class ActividadUsuariosComponent {

  actividades = [
    { id_actividad: 1, id_usuario: 1, rol_usuario: 'Administrador', fecha_acceso: '2025-09-23 08:00' },
    { id_actividad: 2, id_usuario: 2, rol_usuario: 'Docente', fecha_acceso: '2025-09-23 08:00' },
    { id_actividad: 3, id_usuario: 3, rol_usuario: 'Alumno', fecha_acceso: '2025-09-23 08:00' },
    { id_actividad: 4, id_usuario: 4, rol_usuario: 'Alumno', fecha_acceso: '2025-09-23 08:00' },
    { id_actividad: 5, id_usuario: 5, rol_usuario: 'Alumno', fecha_acceso: '2025-09-23 08:00' },
    { id_actividad: 6, id_usuario: 6, rol_usuario: 'Alumno', fecha_acceso: '2025-09-23 08:00' },
    { id_actividad: 7, id_usuario: 7, rol_usuario: 'Alumno', fecha_acceso: '2025-09-23 08:00' },
    { id_actividad: 8, id_usuario: 8, rol_usuario: 'Alumno', fecha_acceso: '2025-09-23 08:00' },
    { id_actividad: 9, id_usuario: 9, rol_usuario: 'Alumno', fecha_acceso: '2025-09-23 08:00' },
    { id_actividad: 10, id_usuario: 10, rol_usuario: 'Alumno', fecha_acceso: '2025-09-23 08:00' },
   
  ];

  constructor(private router: Router) {}

  regresar() {
    this.router.navigate(['inicio']);
  }
}
