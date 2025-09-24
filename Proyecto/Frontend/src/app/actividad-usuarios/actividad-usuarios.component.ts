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
    { id_usuario: 1, rol_usuario: 'Administrador', fecha_acceso: '2025-09-23 08:30', resultado: 'Exitoso' },
    { id_usuario: 2, rol_usuario: 'Docente', fecha_acceso: '2025-09-23 09:10', resultado: 'Fallido' },
    { id_usuario: 3, rol_usuario: 'Alumno', fecha_acceso: '2025-09-23 09:45', resultado: 'Exitoso' },
    { id_usuario: 4, rol_usuario: 'Administrador', fecha_acceso: '2025-09-23 10:00', resultado: 'Exitoso' },
    { id_usuario: 5, rol_usuario: 'Docente', fecha_acceso: '2025-09-23 10:20', resultado: 'Exitoso' },
    { id_usuario: 6, rol_usuario: 'Alumno', fecha_acceso: '2025-09-23 11:05', resultado: 'Fallido' },
    { id_usuario: 7, rol_usuario: 'Docente', fecha_acceso: '2025-09-23 11:30', resultado: 'Exitoso' },
    { id_usuario: 8, rol_usuario: 'Alumno', fecha_acceso: '2025-09-23 12:15', resultado: 'Exitoso' },
    { id_usuario: 9, rol_usuario: 'Administrador', fecha_acceso: '2025-09-23 12:40', resultado: 'Fallido' },
    { id_usuario: 10, rol_usuario: 'Docente', fecha_acceso: '2025-09-23 13:00', resultado: 'Exitoso' }
  ];

  constructor(private router: Router) {}

  regresar() {
    this.router.navigate(['inicio']);
  }
}
