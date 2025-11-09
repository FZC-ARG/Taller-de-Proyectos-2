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
  alumnos: any[] = [];
  docentes: any[] = [];
  admins: any[] = [];

  nuevoAlumno: any = {
    apellido: '',
    contrasena: '',
    fechaNacimiento: '',
    nombre: '',
    nombreUsuario: ''
  };

  constructor(private router: Router , private adminService: AdminService) {}

  ngOnInit() {
    this.datosAdmin = JSON.parse(localStorage.getItem('datosAdmin')!);
    if (this.datosAdmin?.idAdmin) {
      this.cargarAlumnos();
      this.cargarDocentes();
      this.cargarAdmins();
    }
  }

  cargarAlumnos() {
    this.adminService.getAlumnos().subscribe({
      next: (data) => this.alumnos = data,
      error: (err) => console.error('Error al obtener alumnos:', err)
    });
  }

  cargarDocentes() {
    this.adminService.getDocentes().subscribe({
      next: (data) => this.docentes = data,
      error: (err) => console.error('Error al obtener docentes:', err)
    });
  }
  cargarAdmins() {
    this.adminService.getAdmins().subscribe({
      next: (data) => this.admins = data,
      error: (err) => console.error('Error al obtener admins:', err)
    });
  }

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

  // 🟢 Modal para nuevo alumno
  async nuevoAlumnoModal() {
    const { value: formValues } = await Swal.fire({
      title: 'Registrar nuevo alumno',
      html: `
        <input id="nombre" class="swal2-input" placeholder="Nombre">
        <input id="apellido" class="swal2-input" placeholder="Apellido">
        <input id="nombreUsuario" class="swal2-input" placeholder="Nombre de usuario">
        <input id="contrasena" class="swal2-input" type="password" placeholder="Contraseña">
        <input id="fechaNacimiento" class="swal2-input" type="date" placeholder="Fecha de nacimiento">
      `,
      focusConfirm: false,
      confirmButtonText: 'Guardar',
      showCancelButton: true,
      preConfirm: () => {
        const nombre = (document.getElementById('nombre') as HTMLInputElement).value;
        const apellido = (document.getElementById('apellido') as HTMLInputElement).value;
        const nombreUsuario = (document.getElementById('nombreUsuario') as HTMLInputElement).value;
        const contrasena = (document.getElementById('contrasena') as HTMLInputElement).value;
        const fechaNacimiento = (document.getElementById('fechaNacimiento') as HTMLInputElement).value;

        if (!nombre || !apellido || !nombreUsuario || !contrasena || !fechaNacimiento) {
          Swal.showValidationMessage('Por favor complete todos los campos');
          return;
        }

        return { nombre, apellido, nombreUsuario, contrasena, fechaNacimiento };
      }
    });

    if (formValues) {
      this.nuevoAlumno = formValues;
      this.guardarAlumno();
    }
  }

  // 🟢 Modal para nuevo docente
  async nuevoDocenteModal() {
    const { value: formValues } = await Swal.fire({
      title: 'Registrar nuevo docente',
      html: `
        <input id="nombre" class="swal2-input" placeholder="Nombre">
        <input id="apellido" class="swal2-input" placeholder="Apellido">
        <input id="nombreUsuario" class="swal2-input" placeholder="Nombre de usuario">
        <input id="contrasena" class="swal2-input" type="password" placeholder="Contraseña">
      `,
      focusConfirm: false,
      confirmButtonText: 'Guardar',
      showCancelButton: true,
      preConfirm: () => {
        const nombre = (document.getElementById('nombre') as HTMLInputElement).value;
        const apellido = (document.getElementById('apellido') as HTMLInputElement).value;
        const nombreUsuario = (document.getElementById('nombreUsuario') as HTMLInputElement).value;
        const contrasena = (document.getElementById('contrasena') as HTMLInputElement).value;

        if (!nombre || !apellido || !nombreUsuario || !contrasena) {
          Swal.showValidationMessage('Por favor complete todos los campos');
          return;
        }

        return { nombre, apellido, nombreUsuario, contrasena };
      }
    });

    if (formValues) {
      this.adminService.newDocente(formValues).subscribe({
        next: () => {
          Swal.fire('Docente guardado', 'El docente ha sido guardado exitosamente', 'success');
          this.cargarDocentes();
        },
        error: (err) => {
          console.error('Error al guardar docente:', err);
          Swal.fire('Error', 'No se pudo guardar el docente', 'error');
        }
      });
    }
  }

  // 🟢 Modal para nuevo admin
  async nuevoAdminModal() {
    const { value: formValues } = await Swal.fire({
      title: 'Registrar nuevo administrador',
      html: `
        <input id="nombre" class="swal2-input" placeholder="Nombre">
        <input id="apellido" class="swal2-input" placeholder="Apellido">
        <input id="nombreUsuario" class="swal2-input" placeholder="Nombre de usuario">
        <input id="contrasena" class="swal2-input" type="password" placeholder="Contraseña">
      `,
      focusConfirm: false,
      confirmButtonText: 'Guardar',
      showCancelButton: true,
      preConfirm: () => {
        const nombre = (document.getElementById('nombre') as HTMLInputElement).value;
        const apellido = (document.getElementById('apellido') as HTMLInputElement).value;
        const nombreUsuario = (document.getElementById('nombreUsuario') as HTMLInputElement).value;
        const contrasena = (document.getElementById('contrasena') as HTMLInputElement).value;

        if (!nombre || !apellido || !nombreUsuario || !contrasena) {
          Swal.showValidationMessage('Por favor complete todos los campos');
          return;
        }

        return { nombre, apellido, nombreUsuario, contrasena };
      }
    });

    if (formValues) {
      this.adminService.newAdmin(formValues).subscribe({
        next: () => {
          this.cargarAdmins();
          Swal.fire('Administrador guardado', 'El administrador ha sido guardado exitosamente', 'success');
        },
        error: (err) => {
          console.error('Error al guardar admin:', err);
          Swal.fire('Error', 'No se pudo guardar el administrador', 'error');
        }
      });
    }
  }

  guardarAlumno() {
    this.adminService.newAlumno(this.nuevoAlumno).subscribe({
      next: () => {
        Swal.fire('Alumno guardado', 'El alumno ha sido guardado exitosamente', 'success');
        this.cargarAlumnos();
      },
      error: (err) => {
        console.error('Error al guardar alumno:', err);
        Swal.fire('Error', 'No se pudo guardar el alumno', 'error');
      }
    });
  }
}
