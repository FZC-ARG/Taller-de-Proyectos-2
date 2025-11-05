import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_URL } from '../conexion';

@Injectable({
  providedIn: 'root'
})
export class DocentesService {
  constructor(private http: HttpClient) {}

  getAlumnosDocente(id: string): Observable<any> {
    return this.http.get(`${API_URL}api/cursos/docente/${id}/alumnos`);
  }
}
