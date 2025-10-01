import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-recuperar-contrasena',
  templateUrl: './recuperar-contrasena.component.html',
  styleUrls: ['./recuperar-contrasena.component.css'],
  standalone: true,
  imports: [FormsModule,CommonModule]
})
export class RecuperarContrasenaComponent {
  nuevaContrasena: string = '';
  confirmarContrasena: string = '';

  constructor(private router: Router) { }

  regresar() {
    this.router.navigate(['/login']);
  }

  confirmar() {
    // Validar campos vacíos
    if (!this.nuevaContrasena || !this.confirmarContrasena) {
      Swal.fire({
        icon: 'error',
        title: 'Campos vacíos',
        text: 'Debe completar ambos campos.'
      });
      return;
    }

    // Validar longitud mínima
    if (this.nuevaContrasena.length < 6) {
      Swal.fire({
        icon: 'error',
        title: 'Contraseña inválida',
        text: 'La contraseña debe tener al menos 6 caracteres.'
      });
      return;
    }

    // Validar que tenga al menos un número
    const regexNumero = /\d/;
    if (!regexNumero.test(this.nuevaContrasena)) {
      Swal.fire({
        icon: 'error',
        title: 'Contraseña insegura',
        text: 'La contraseña debe contener al menos un número.'
      });
      return;
    }

    // Validar que coincidan
    if (this.nuevaContrasena !== this.confirmarContrasena) {
      Swal.fire({
        icon: 'error',
        title: 'Error',
        text: 'Las contraseñas no coinciden.'
      });
      return;
    }

    // Si todo es válido
    Swal.fire({
      icon: 'success',
      title: 'Éxito',
      text: 'Contraseña cambiada con éxito.'
    }).then(() => {
      this.router.navigate(['/login']);
    });
  }
}
