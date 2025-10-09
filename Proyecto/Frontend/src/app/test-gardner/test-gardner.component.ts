import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-test-gardner',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './test-gardner.component.html',
  styleUrls: ['./test-gardner.component.css']
})
export class TestGardnerComponent {
  preguntas = [
    { id: 1, texto: 'Me gusta resolver problemas matemáticos o lógicos.', respuesta: null },
    { id: 2, texto: 'Disfruto participar en actividades musicales.', respuesta: null },
    { id: 3, texto: 'Prefiero aprender a través de imágenes, diagramas o mapas mentales.', respuesta: null },
    { id: 4, texto: 'Puedo expresar mis ideas fácilmente con palabras.', respuesta: null },
    { id: 5, texto: 'Me resulta fácil trabajar en grupo.', respuesta: null },
    { id: 6, texto: 'Me gusta observar la naturaleza y aprender de ella.', respuesta: null },
    { id: 7, texto: 'Soy consciente de mis emociones y cómo influyen en mí.', respuesta: null },
    { id: 8, texto: 'Me gusta moverme y aprender haciendo actividades físicas.', respuesta: null },
  ];

  constructor(private router: Router) {}

  onSubmit() {
    // Validar que todas las preguntas tengan respuesta
    const incompletas = this.preguntas.filter(p => p.respuesta === null);
    if (incompletas.length > 0) {
      Swal.fire({
        icon: 'warning',
        title: 'Faltan respuestas',
        text: 'Por favor responde todas las preguntas antes de enviar.',
      });
      return;
    }

    // En esta parte en el futuro enviarás las respuestas a la base de datos
    console.log('Respuestas del test:', this.preguntas);

    Swal.fire({
      icon: 'success',
      title: 'Respuestas guardadas',
      text: 'El resultado se está calculando...',
      timer: 2500,
      showConfirmButton: false
    }).then(() => {
      this.router.navigate(['/inicio-alumnos']);
    });
  }
}
