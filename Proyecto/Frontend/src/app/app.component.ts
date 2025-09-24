import { Component, HostListener } from '@angular/core';
import { Router } from '@angular/router';
import { RouterOutlet } from '@angular/router';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
})
export class AppComponent {
  title = 'Proyecto_IA';
  private timeoutId: any;
  private readonly inactivityTime = 15 * 60 * 1000; // 15 minutos

  constructor(private router: Router) {
    this.resetTimer();
  }

  @HostListener('document:mousemove')
  @HostListener('document:keydown')
  @HostListener('document:click')
  @HostListener('document:scroll')
  resetTimer() {
    // si estoy en /login, no hago nada
    if (this.router.url === '/login') {
      clearTimeout(this.timeoutId);
      return;
    }

    clearTimeout(this.timeoutId);
    this.timeoutId = setTimeout(() => this.logout(), this.inactivityTime);
  }

  logout() {
    // solo cerrar sesión si NO está en /login
    if (this.router.url !== '/login') {
      localStorage.clear();
      this.router.navigate(['/login']);
      Swal.fire({
        title: 'Sesión cerrada',
        text: 'Se detecto inactividad, Se ha cerrado la sesión',
        icon: 'warning',
        confirmButtonText: 'Aceptar',
      });
    }
  }
}
