import { Component, ElementRef, ViewChild, AfterViewChecked } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router ,ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-chatbot',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './chatbot.component.html',
  styleUrls: ['./chatbot.component.css']
})
export class ChatbotComponent implements AfterViewChecked {

  constructor( private router: Router, private route: ActivatedRoute) {}
  mensajes: any[] = [];
  mensajeActual = '';



  @ViewChild('chatContainer') chatContainer!: ElementRef;

  enviarMensaje() {
    if (!this.mensajeActual.trim()) return;

    this.mensajes.push({ texto: this.mensajeActual, emisor: 'usuario' });
    this.scrollSuave();

    setTimeout(() => {
      this.mensajes.push({
        texto: 'Hola 👋 Soy tu asistente virtual. ¿Cómo puedo ayudarte?',
        emisor: 'bot'
      });
      this.scrollSuave();
    }, 600);

    this.mensajeActual = '';
  }

  scrollSuave() {
    try {
      const contenedor = this.chatContainer.nativeElement;
      contenedor.scrollTo({
        top: contenedor.scrollHeight,
        behavior: 'smooth'
      });
    } catch {}
  }

  ngAfterViewChecked() {
    this.scrollSuave();
  }

  verPerfil() {
    const idAlumno = this.route.snapshot.paramMap.get('idAlumno');
    this.router.navigate(['/perfil-alumno', idAlumno]);
  }
}
