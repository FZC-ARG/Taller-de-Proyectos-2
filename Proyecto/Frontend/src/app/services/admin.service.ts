import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_URL } from '../conexion';

@Injectable({
  providedIn: 'root'
})
export class AdminService {
  constructor(private http: HttpClient) {}

  getAlumnos(): Observable<any> {
    const url = `${API_URL}api/admin/alumnos`;
    return this.http.get<any>(url);
  }

  getDocentes(): Observable<any> {
    const url = `${API_URL}api/admin/docentes`;
    return this.http.get<any>(url);
  }

  getAdmins(): Observable<any> {
    const url = `${API_URL}api/admin/administradores`;
    return this.http.get<any>(url);
  }

  newAlumno(data: any): Observable<any> {
    const url = `${API_URL}api/admin/alumnos`;
    return this.http.post<any>(url, data);
  }

  newDocente(data: any): Observable<any> {
    const url = `${API_URL}api/admin/docentes`;
    return this.http.post<any>(url, data);
  }

  newAdmin(data: any): Observable<any> {    
    const url = `${API_URL}api/admin/administradores`;
    return this.http.post<any>(url, data);
  }
  
  editarAlumno(data: any): Observable<any> {
    const url = `${API_URL}api/admin/alumnos/${data.idAlumno}`;
    return this.http.put<any>(url, data);
  }

  editarDocente(data: any): Observable<any> {
    const url = `${API_URL}api/admin/docentes/${data.idDocente}`;
    return this.http.put<any>(url, data);
  }

  editarAdmin(data: any): Observable<any> {
    const url = `${API_URL}api/admin/administradores/${data.idAdmin}`;
    return this.http.put<any>(url, data);
  }

  eliminarAlumno(id: string): Observable<any> {
    const url = `${API_URL}api/admin/alumnos/${id}`;
    return this.http.delete<any>(url);
  }

  eliminarDocente(id: string): Observable<any> {
    const url = `${API_URL}api/admin/docentes/${id}`;
    return this.http.delete<any>(url);
  }

  eliminarAdmin(id: string): Observable<any> {
    const url = `${API_URL}api/admin/administradores/${id}`;
    return this.http.delete<any>(url);
  } 
  
  
}
