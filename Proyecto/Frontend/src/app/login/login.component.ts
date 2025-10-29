import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { LoginService } from '../services/login.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, HttpClientModule, FormsModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  usuario: string = '';
  contrasena: string = '';
  tipoLogin: string = 'docente'; // por defecto
  animar: boolean = false;
  exito: boolean = false;
  
  constructor(private router: Router, private loginService: LoginService) { }

  setLogin(tipo: string) {
    this.animar = true;
    setTimeout(() => {
      this.tipoLogin = tipo;
      this.animar = false;
    }, 300);
  }

  login() {
    this.exito = false; // reinicia estado

    if (this.tipoLogin === 'docente') {
      this.loginService.loginDocente(this.usuario, this.contrasena).subscribe({
        next: (response) => {
          this.exito = true;
          Swal.fire({
            title: 'Ingreso exitoso',
            text: 'Ingreso como docente',
            icon: 'success',
            confirmButtonText: 'Aceptar'
          }).then(() => {
            this.router.navigate(['inicio-docentes']);
          });
        },
        error: () => {
          this.mostrarError();
        }
      });

    } else if (this.tipoLogin === 'alumno') {
      this.loginService.loginAlumno(this.usuario, this.contrasena).subscribe({
        next: (response) => {
          this.exito = true;
          Swal.fire({
            title: 'Ingreso exitoso',
            text: 'Ingreso como alumno',
            icon: 'success',
            confirmButtonText: 'Aceptar'
          }).then(() => {
            this.router.navigate(['inicio-alumnos']);
          });
        },
        error: () => {
          this.mostrarError();
        }
      });

    } else if (this.tipoLogin === 'admin') {
      this.loginService.loginAdmin(this.usuario, this.contrasena).subscribe({
        next: (response) => {
          this.exito = true;
          Swal.fire({
            title: 'Ingreso exitoso',
            text: 'Ingreso como administrador',
            icon: 'success',
            confirmButtonText: 'Aceptar'
          }).then(() => {
            this.router.navigate(['inicio']);
          });
        },
        error: () => {
          this.mostrarError();
        }
      });
    }
  }

  mostrarError() {
    Swal.fire({
      title: 'Error',
      html: `Usuario o contraseña incorrectos.<br>
            Estás intentando ingresar como <b>${this.tipoLogin}</b>.<br>
            Si no es correcto, cambia el modo de ingreso.`,
      icon: 'error',
      confirmButtonText: 'Aceptar',
    });
  }

  recuperar() {
    this.router.navigate(['/recuperar-contrasena'], { queryParams: { rol: this.tipoLogin } });
  }
}
