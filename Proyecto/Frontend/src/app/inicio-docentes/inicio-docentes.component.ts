import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { DocentesService } from '../services/docentes.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-inicio-docentes',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './inicio-docentes.component.html',
  styleUrls: ['./inicio-docentes.component.css']
})
export class InicioDocentesComponent implements OnInit {

  constructor(private router: Router, private docentesService: DocentesService) {}

  seccionActual: 'alumnos' | 'test' = 'alumnos';
  modalAbierto = false;
  datosDocente: any;
  alumnos: any[] = []; // <--- array vacío

  ngOnInit() {
    this.datosDocente = JSON.parse(localStorage.getItem('datosDocente')!);
    console.log(this.datosDocente);

    if (this.datosDocente?.idDocente) {
      this.cargarAlumnos(this.datosDocente.idDocente);
    }
  }

  cargarAlumnos(idDocente: string): void {
    this.docentesService.getAlumnosDocente(idDocente).subscribe({
      next: (data) => {
        this.alumnos = data;
        console.log('Alumnos cargados:', this.alumnos);
      },
      error: (err) => {
        console.error('Error al obtener alumnos:', err);
      }
    });
  }

  mostrarPerfil(id: string, nombre: string) {
    this.router.navigate(['/perfil-alumno'] );
  }

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
    window.open('https://utecno.wordpress.com/wp-content/uploads/2014/07/howard_gardner_-_estructuras_de_la_mente.pdf', '_blank');
  }

  mostrarChatbot(idDocente: string, idAlumno: string) {
    this.router.navigate(['/chatbot', idDocente, idAlumno]);
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
