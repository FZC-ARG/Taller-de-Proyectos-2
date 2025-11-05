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

 
  
  
}
