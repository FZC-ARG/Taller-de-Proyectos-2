import { Injectable } from '@angular/core';
import {API_URL} from '../conexion';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AlumnosService {

  constructor(private http: HttpClient) { }

  getCursosAlumno(idAlumno: string): Observable<any> {
    return this.http.get(`${API_URL}api/cursos/alumno/${idAlumno}/cursos`);
  }

  getAlumnoById(idAlumno: string): Observable<any> {
    return this.http.get(`${API_URL}api/alumno/${idAlumno}`);
  }

}