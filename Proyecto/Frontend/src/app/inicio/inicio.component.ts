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
      this.listarCursos();
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

  async verUsuarioModal(usuario: any, tipo: 'alumno' | 'docente' | 'admin') {
      const { value: action } = await Swal.fire({
      title: `${tipo.charAt(0).toUpperCase() + tipo.slice(1)}: ${usuario.nombre} ${usuario.apellido}`,
      html: `
        <p><strong>Nombre de usuario:</strong> ${usuario.nombreUsuario}</p>
        ${usuario.fechaNacimiento ? `<p><strong>Fecha de nacimiento:</strong> ${usuario.fechaNacimiento}</p>` : ''}
      `,
      showCancelButton: true,
      showDenyButton: true,
      showConfirmButton: true,
      confirmButtonText: 'Editar',
      denyButtonText: 'Eliminar',
      cancelButtonText: 'Recuperar contraseña',
      confirmButtonColor: '#3085d6',
      denyButtonColor: '#d33',
      cancelButtonColor: '#f6c23e',
    });

    if (action === true) {
      // 🟢 Editar
      const { value: formValues } = await Swal.fire({
        title: `Editar ${tipo}`,
        html: `
          <input id="nombre" class="swal2-input" placeholder="Nombre" value="${usuario.nombre}">
          <input id="apellido" class="swal2-input" placeholder="Apellido" value="${usuario.apellido}">
          <input id="nombreUsuario" class="swal2-input" placeholder="Nombre de usuario" value="${usuario.nombreUsuario}">
          ${tipo === 'alumno' ? `<input id="fechaNacimiento" class="swal2-input" type="date" value="${usuario.fechaNacimiento || ''}">` : ''}
        `,
        confirmButtonText: 'Guardar cambios',
        showCancelButton: true,
        preConfirm: () => {
          const nombre = (document.getElementById('nombre') as HTMLInputElement).value;
          const apellido = (document.getElementById('apellido') as HTMLInputElement).value;
          const nombreUsuario = (document.getElementById('nombreUsuario') as HTMLInputElement).value;
          const fechaNacimiento = (document.getElementById('fechaNacimiento') as HTMLInputElement)?.value;

          if (!nombre || !apellido || !nombreUsuario) {
            Swal.showValidationMessage('Por favor complete todos los campos');
            return;
          }
          return { ...usuario, nombre, apellido, nombreUsuario, fechaNacimiento };
        }
      });

      if (formValues) {
        if (tipo === 'alumno') {
          this.adminService.editarAlumno(formValues).subscribe({
            next: () => {
              Swal.fire('Actualizado', 'Alumno actualizado correctamente', 'success');
              this.cargarAlumnos();
            },
            error: (err) => Swal.fire('Error', 'No se pudo actualizar el alumno', 'error')
          });
        }

        if (tipo === 'docente') {
          this.adminService.editarDocente(formValues).subscribe({
            next: () => {
              Swal.fire('Actualizado', 'Docente actualizado correctamente', 'success');
              this.cargarDocentes();
            },
            error: (err) => Swal.fire('Error', 'No se pudo actualizar el docente', 'error')
          });
        }

        if (tipo === 'admin') {
          this.adminService.editarAdmin(formValues).subscribe({
            next: () => {
              Swal.fire('Actualizado', 'Administrador actualizado correctamente', 'success');
              this.cargarAdmins();
            },
            error: (err) => Swal.fire('Error', 'No se pudo actualizar el administrador', 'error')
          });
        }
        Swal.fire('Actualizado', `${tipo} actualizado correctamente`, 'success');
      }
    } else if (action === false) {
      // 🟥 Eliminar
      const confirm = await Swal.fire({
        title: `¿Eliminar ${tipo}?`,
        text: `Esta acción no se puede deshacer.`,
        icon: 'warning',
        showCancelButton: true,
        confirmButtonText: 'Sí, eliminar',
        cancelButtonText: 'Cancelar'
      });

      if (confirm.isConfirmed) {
        if (tipo === 'alumno') this.adminService.eliminarAlumno(usuario.idAlumno).subscribe(() => this.cargarAlumnos());
        if (tipo === 'docente') this.adminService.eliminarDocente(usuario.idDocente).subscribe(() => this.cargarDocentes());
        if (tipo === 'admin') this.adminService.eliminarAdmin(usuario.idAdmin).subscribe(() => this.cargarAdmins());
        Swal.fire('Eliminado', `${tipo} eliminado correctamente`, 'success');
      }
    } else if (action.dismiss === Swal.DismissReason.cancel) {
      // 🟡 Recuperar contraseña (solo si se presiona el botón)
      this.recuperarContrasena();
    }
  }

  cursos: any[] = [];
  listarCursos() {
    this.adminService.getCursos().subscribe({
      next: (data) => this.cursos = data,
      error: (err) => console.error('Error al obtener cursos:', err)
    });
  }

  async verCursoModal(curso: any) {
    const { value: action } = await Swal.fire({ 
      title: `${curso.nombre}`,
      html: `
        <p><strong>Descripción:</strong> ${curso.descripcion}</p>
        <p><strong>Docente Encargado:</strong> ${curso.nombreDocente}</p>
        
      `,
      showConfirmButton: true,
      confirmButtonText: 'Cerrar',
    
      confirmButtonColor: '#2182ddff',
    });
  }

  async verModalNuevoCurso() {
    // 🔹 Generar opciones de docentes en HTML
    const opcionesDocentes = this.docentes
      .map(docente => `<option value="${docente.idDocente}">${docente.nombre} ${docente.apellido}</option>`)
      .join('');

    const { value: formValues } = await Swal.fire({
      title: 'Registrar nuevo curso',
      html: `
        <input id="nombreCurso" class="swal2-input" placeholder="Nombre del curso">
        <input id="descripcion" class="swal2-input" placeholder="Descripción">
        <select id="idDocente" class="swal2-input">
          <option value="">Seleccione un docente</option>
          ${opcionesDocentes}
        </select>
      `,
      focusConfirm: false,
      confirmButtonText: 'Guardar',
      showCancelButton: true,
      preConfirm: () => {
        const nombreCurso = (document.getElementById('nombreCurso') as HTMLInputElement).value;
        const descripcion = (document.getElementById('descripcion') as HTMLInputElement).value;
        const idDocente = (document.getElementById('idDocente') as HTMLSelectElement).value;

        if (!nombreCurso || !descripcion || !idDocente) {
          Swal.showValidationMessage('Por favor complete todos los campos');
          return;
        }

        return { nombreCurso, descripcion, idDocente };
      }
    });

    if (formValues) {
      this.adminService.newCurso(formValues).subscribe({
        next: () => {
          Swal.fire('Curso guardado', 'El curso ha sido guardado exitosamente', 'success');
          this.listarCursos();
        },
        error: (err) => {
          console.error('Error al guardar curso:', err);
          Swal.fire('Error', 'No se pudo guardar el curso', 'error');
        }
      });
    }
  }
  
  async verModalMatricular() {
    const { value: formValues } = await Swal.fire({
      title: 'Matricular alumno',
      html: `
        <select id="idAlumnoFk" class="swal2-input">
          <option value="">Seleccione un alumno</option>   
          ${this.alumnos.map(alumno => `<option value="${alumno.idAlumno}">${alumno.nombre} ${alumno.apellido}</option>`).join('')}       
        </select>
        <select id="idCursoFk" class="swal2-input">
          <option value="">Seleccione un curso</option>  
          ${this.cursos.map(curso => `<option value="${curso.idCurso}">${curso.nombreCurso}</option>`).join('')}        
        </select>
      `,
      focusConfirm: false,
      confirmButtonText: 'Guardar',
      showCancelButton: true,
      preConfirm: () => {
        const idAlumnoFk = (document.getElementById('idAlumnoFk') as HTMLSelectElement).value;
        const idCursoFk = (document.getElementById('idCursoFk') as HTMLSelectElement).value;

        if (!idAlumnoFk || !idCursoFk) {
          Swal.showValidationMessage('Por favor complete todos los campos');
          return;
        }

        return { idAlumnoFk, idCursoFk };
      }
    });

    if (formValues) {
      this.adminService.matricularAlumno(formValues).subscribe({
        next: () => {
          Swal.fire('Alumno matriculado', 'El alumno ha sido matriculado exitosamente', 'success');
          this.listarCursos();
        },
        error: (err) => {
          console.error('Error al guardar curso:', err);
          Swal.fire('Error', 'No se pudo guardar el curso', 'error');
        }
      });
    }     
  }

  // Variables para consultas
  idDocenteCursos: string = '';
  idCursoAlumnos: string = '';
  idAlumnoCursos: string = '';
  idDocenteAlumnos: string = '';
  resultados: any[] = [];

  listarCursosDeDocente(): void {
    if (!this.idDocenteCursos) {
      Swal.fire('Seleccione un docente', '', 'warning');
      return;
    }
    this.adminService.listarCursosDocente(this.idDocenteCursos).subscribe({
      next: (data) => this.resultados = data,
      error: () => Swal.fire('Error', 'No se pudieron obtener los cursos', 'error')
    });
  }

  listarAlumnosDelCurso(): void {
    if (!this.idCursoAlumnos) {
      Swal.fire('Seleccione un curso', '', 'warning');
      return;
    }
    this.adminService.listarAlumnosCurso(this.idCursoAlumnos).subscribe({
      next: (data) => this.resultados = data,
      error: () => Swal.fire('Error', 'No se pudieron obtener los alumnos', 'error')
    });
  }

  listarCursosDelAlumno(): void {
    if (!this.idAlumnoCursos) {
      Swal.fire('Seleccione un alumno', '', 'warning');
      return;
    }
    this.adminService.listarCursosAlumno(this.idAlumnoCursos).subscribe({
      next: (data) => this.resultados = data,
      error: () => Swal.fire('Error', 'No se pudieron obtener los cursos', 'error')
    });
  }

  listarAlumnosDelDocente(): void {
    if (!this.idDocenteAlumnos) {
      Swal.fire('Seleccione un docente', '', 'warning');
      return;
    }
    this.adminService.listarAlumnosDocente(this.idDocenteAlumnos).subscribe({
      next: (data) => this.resultados = data,
      error: () => Swal.fire('Error', 'No se pudieron obtener los alumnos', 'error')
    });
  }

}
