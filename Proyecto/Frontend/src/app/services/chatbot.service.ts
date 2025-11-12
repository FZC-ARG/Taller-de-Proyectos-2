import { Injectable } from '@angular/core';
import { API_URL } from '../conexion';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ChatbotService {

  constructor(private http: HttpClient) { }

  getSesion(idDocente: string, idAlumno: string): Observable<any> {
    return this.http.get(`${API_URL}api/chat/sesiones/docente/${idDocente}/alumno/${idAlumno}/ultima`);
  }

  getMensajes(idSesion: string): Observable<any> {
    return this.http.get(`${API_URL}api/chat/sesiones/${idSesion}/mensajes`);
  }

  enviarMensaje(idSesion: string , contenido: string): Observable<any> {
    return this.http.post(`${API_URL}api/chat/sesiones/${idSesion}/mensajes`, { contenido });
  } 

  nuevaSesion(idDocente: string, idAlumno: string): Observable<any> {
    const body = {
      forzarCreacion: true,
      idAlumno,
      idCurso: null,
      idDocente,
      tituloSesion: 'Consulta Individual'
    };

    return this.http.post(`${API_URL}api/chat/sesiones`, body);
  }


}
