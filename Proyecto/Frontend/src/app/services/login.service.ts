import { Injectable } from '@angular/core';
import { API_URL } from '../conexion';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  constructor(private http: HttpClient) { }

  loginAdmin(nombreUsuario: string, contrasena: string): Observable<any> {
    return this.http.post<any>(`${API_URL}api/auth/login/admin`, { nombreUsuario, contrasena });
  }

  loginDocente(nombreUsuario: string, contrasena: string): Observable<any> {
    return this.http.post<any>(`${API_URL}api/auth/login/docente`, { nombreUsuario, contrasena });
  }

  loginAlumno(nombreUsuario: string, contrasena: string): Observable<any> {
    return this.http.post<any>(`${API_URL}api/auth/login/alumno`, { nombreUsuario, contrasena });
  }
}
