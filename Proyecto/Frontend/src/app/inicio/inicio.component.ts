import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { DocentesService } from '../services/docentes.service';
import Swal from 'sweetalert2';


@Component({
  selector: 'app-inicio',
  standalone: true,
  imports: [CommonModule, HttpClientModule, FormsModule],
  templateUrl: './inicio.component.html',
  styleUrls: ['./inicio.component.css']
})
export class InicioComponent {
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
}
