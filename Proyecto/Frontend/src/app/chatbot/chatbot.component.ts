import { Component, ElementRef, ViewChild, AfterViewChecked, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { ChatbotService } from '../services/chatbot.service';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';
import { AlumnosService } from '../services/alumnos.service';

@Component({
  selector: 'app-chatbot',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './chatbot.component.html',
  styleUrls: ['./chatbot.component.css']
})
export class ChatbotComponent implements OnInit {

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private chatbotService: ChatbotService,
    private sanitizer: DomSanitizer,
    private alumnosService: AlumnosService
  ) {}

  mensajes: { texto: SafeHtml; emisor: string }[] = [];
  mensajeActual = '';
  idDocente!: string;
  idAlumno!: string;
  idSesion!: string;
  cargando = false;


  @ViewChild('chatContainer') chatContainer!: ElementRef;

  ngOnInit(): void {
    this.idDocente = this.route.snapshot.paramMap.get('idDocente')!;
    this.idAlumno = this.route.snapshot.paramMap.get('idAlumno')!;
    this.cargarSesion();
  }   
  cargarSesion() {
    this.chatbotService.getSesion(this.idDocente, this.idAlumno).subscribe({
      next: (sesion) => {
        this.idSesion = sesion.idSesion;
        this.cargarMensajes();
      },
      error: (err) => console.error('Error al obtener la sesión:', err)
    });
  }

  cargarMensajes() {
    this.chatbotService.getMensajes(this.idSesion).subscribe({
      next: (data) => {
        this.mensajes = data.map((m: any) => ({
          texto: this.sanitizer.bypassSecurityTrustHtml(
            this.formatearTexto(m.contenido)
          ),
          emisor: m.emisor === 'docente' ? 'usuario' : 'bot'
        }));
      },
      error: (err) => console.error('Error al obtener mensajes:', err)
    });
  }

  enviarMensaje() {
    if (!this.mensajeActual.trim()) return;

    const contenido = this.mensajeActual;
    this.mensajes.push({ texto: contenido, emisor: 'usuario' });
    this.mensajeActual = '';

    // 🔹 Mostrar "pensando..."
    this.cargando = true;

    this.chatbotService.enviarMensaje(this.idSesion, contenido).subscribe({
      next: (respuesta) => {

        // Quitar loader
        this.mensajes = this.mensajes.filter(m => m.emisor !== 'loader');
        this.cargando = false;

        this.mensajes.push({
          texto: this.sanitizer.bypassSecurityTrustHtml(
            this.formatearTexto(respuesta.contenido)
          ),
          emisor: 'bot'
        });
      },
      error: (err) => console.error('Error al enviar mensaje:', err)
    });
  }

  formatearTexto(texto: string): string {
    // 🔹 Eliminar la secuencia específica de DeepSeek (<｜begin▁of▁sentence｜>)
    texto = texto.replace(/<[\uFF5C|]begin[\u2581_]?of[\u2581_]?sentence[\uFF5C|]>/gi, '');

    // 🔹 Limpiar otros símbolos raros
    texto = texto
      .replace(/�/g, '')
      .replace(/\s+$/, '');

    // 🔹 Títulos Markdown ###
    texto = texto.replace(/^### (.*$)/gim, '<h3>$1</h3>');
    texto = texto.replace(/^## (.*$)/gim, '<h2>$1</h2>');
    texto = texto.replace(/^# (.*$)/gim, '<h1>$1</h1>');

    // 🔹 Negritas
    texto = texto.replace(/\*\*(.*?)\*\*/g, '<b>$1</b>');

    // 🔹 Listas con - o *
    texto = texto.replace(/(?:\n|^)[\-•]\s?(.*?)(?=\n|$)/g, '<li>$1</li>');
    if (texto.includes('<li>')) {
      texto = texto.replace(/(<li>[\s\S]*?<\/li>)/g, '<ul>$1</ul>');
    }

    // 🔹 Saltos de línea
    texto = texto.replace(/\n+/g, '<br>');

    return texto.trim();
  }

  
    private autoScroll = true;

  ngAfterViewChecked() {
    if (this.autoScroll && this.chatContainer) {
      const el = this.chatContainer.nativeElement;
      el.scrollTop = el.scrollHeight;
    }
  }
  
  onScroll() {
    const el = this.chatContainer.nativeElement;
    // Si el usuario está casi al fondo, activamos el auto-scroll
    this.autoScroll = el.scrollTop + el.clientHeight >= el.scrollHeight - 50;
  }
  verPerfil() {
    this.router.navigate(['/perfil-alumno', this.idAlumno]);
  }

  regresar() {
    this.router.navigate(['/inicio-docentes']);
  }

  crearNuevaSesion() {
    if (!this.idDocente || !this.idAlumno) return;

    this.cargando = true;
    this.mensajes = [];
    this.mensajeActual = '';

    this.chatbotService.nuevaSesion(this.idDocente, this.idAlumno).subscribe({
      next: (nueva) => {
        this.idSesion = nueva.idSesion;
        this.cargando = false;

        // Mensaje informativo o inicial del bot
        this.mensajes.push({
          texto: this.sanitizer.bypassSecurityTrustHtml(
            `<i>Se ha iniciado una nueva sesión de chat titulada <b>"Consulta Individual"</b>.</i>`
          ),
          emisor: 'bot'
        });
      },
      error: (err) => {
        console.error('Error al crear nueva sesión:', err);
        this.cargando = false;
      }
    });
  }
}
