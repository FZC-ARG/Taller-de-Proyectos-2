import { Injectable } from '@angular/core';
import { API_URL } from '../conexion';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TestService {

  constructor(private http: HttpClient) { }

  getPreguntas(): Observable<any> {
    return this.http.get<any>(`${API_URL}api/test/preguntas`);
  }

  enviarResultados(data: any) {
  return this.http.post(`${API_URL}api/test/resultados`, data, {
        responseType: 'text'  // 👈 esto evita el error de parseo JSON
      });
  }

  getResultados(idAlumno: string): Observable<any> {
    return this.http.get(`${API_URL}api/test/resultados/alumno/${idAlumno}`);
  }
}
