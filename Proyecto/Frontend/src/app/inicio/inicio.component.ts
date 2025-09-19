import { Component, OnInit } from '@angular/core';
import { Router, RouterModule, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { AlumnosService } from '../services/alumnos.service';

@Component({
  selector: 'app-inicio',
  standalone: true,
  imports: [FormsModule, CommonModule, RouterModule, RouterLink],
  templateUrl: './inicio.component.html',
  styleUrl: './inicio.component.css'
})
export class InicioComponent implements OnInit {
  constructor(private router: Router, private alumnosService: AlumnosService) {}

  alumnos: { id: number, nombre: string }[] = [];
  filtro: string = '';
  escribiendo: boolean = false;
  nombreDocente: string = '';

  ngOnInit(): void {
    
  }
  
  get alumnosFiltrados() {
    return this.alumnos.filter(a =>
      a.nombre.toLowerCase().includes(this.filtro.toLowerCase())
    );
  }

  goToAlumno(id: number) {
    this.router.navigate(['/alumno', id]);
  }

  salir() {
    localStorage.removeItem('token');
    localStorage.removeItem('nombre');
    this.router.navigate(['/login']);
  }
}
