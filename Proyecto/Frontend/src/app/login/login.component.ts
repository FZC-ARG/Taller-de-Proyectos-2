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
  dni: string = '';
  contrasena: string = '';

  constructor(private router: Router, private docentesService: DocentesService) {}

  login() {
    this.router.navigate(['inicio']);
    Swal.fire({
      title: 'Ingreso exitoso',
      text: 'Su ingreso fue exitoso',
      icon: 'success',
      confirmButtonText: 'Aceptar',
    });
  }

}
