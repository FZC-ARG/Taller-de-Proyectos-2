import { Injectable } from '@angular/core';
import { API_URL } from '../conexion';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class RegistroActividadService {

  constructor(private http: HttpClient) { }

  getRegistroActividad(): Observable<any> {
    return this.http.get<any>(`${API_URL}api/auth/logs`);
  }
}
