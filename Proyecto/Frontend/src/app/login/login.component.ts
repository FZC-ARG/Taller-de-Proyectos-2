import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { DocentesService } from '../services/docentes.service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, HttpClientModule, FormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  usuario: string = '';
  contrasena: string = '';
  tipoLogin: string = 'docente'; // por defecto
  animar: boolean = false;
  exito: boolean = false;

  constructor(private router: Router, private docentesService: DocentesService) {}

  setLogin(tipo: string) {
    this.animar = true; // activa animación
    setTimeout(() => {
      this.tipoLogin = tipo;
      this.animar = false; // termina animación
    }, 300);
  }

  login() {
    if (this.tipoLogin === 'docente') {
      // lógica para docentes
      if (this.usuario =='docente1' && this.contrasena == '1234') {
        this.exito = true;
        this.router.navigate(['inicio-docentes']);
      }
    } else if (this.tipoLogin === 'alumno') {
      // lógica para alumnos
      if (this.usuario =='alumno1' && this.contrasena == '1234') {
        this.exito = true;
        this.router.navigate(['inicio-alumnos']);
      }
    } else if (this.tipoLogin === 'admin') {
      // lógica para administrador
      if (this.usuario =='admin1' && this.contrasena == '1234') {
        this.exito = true;
        this.router.navigate(['inicio']);
      }
    }

    if(this.exito==true)
    {
      Swal.fire({
        title: 'Ingreso exitoso',
        text: `Ingreso como ${this.tipoLogin}`,
        icon: 'success',
        confirmButtonText: 'Aceptar',
      });
    }
    else
    {
      Swal.fire({
        title: 'Error',
        html: 'Usuario o contraseña incorrectos.<br><b>Asegúrese de seleccionar su rol correcto',
        icon: 'error',
        confirmButtonText: 'Aceptar',
      });
    }
  }

  recuperar() {
    this.router.navigate(['/recuperar-contrasena']);
  }
}
