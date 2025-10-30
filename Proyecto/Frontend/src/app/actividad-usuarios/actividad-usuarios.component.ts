import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { RegistroActividadService } from '../services/registroActividad.service';

@Component({
  selector: 'app-actividad-usuarios',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './actividad-usuarios.component.html',
  styleUrls: ['./actividad-usuarios.component.css']
})
export class ActividadUsuariosComponent implements OnInit {

  actividades: any[] = [];

  constructor(
    private router: Router,
    private registroActividadService: RegistroActividadService
  ) {}

  ngOnInit(): void {
    this.cargarActividades();
  }

  cargarActividades(): void {
    this.registroActividadService.getRegistroActividad().subscribe({
      next: (data) => {
        this.actividades = data.reverse();
        console.log('Actividades obtenidas:', this.actividades);
      },
      error: (err) => {
        console.error('Error al obtener registros de actividad:', err);
      }
    });
  }

  regresar(): void {
    this.router.navigate(['inicio']);
  }
}
