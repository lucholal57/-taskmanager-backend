import { Injectable } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { Auth } from '../model/auth.model';
import { HttpClient } from '@angular/common/http';


@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private apiUrl = 'http://localhost:8080/api/auth/login';

  constructor(private http: HttpClient) { }

  login(data: Auth.LoginRequest): Observable<Auth.LoginResponse> {
    return this.http.post<Auth.LoginResponse>(this.apiUrl, data).pipe(
      tap((res) => {
        console.log('Respuesta del servidor:', res);
        // Si el backend envía "Bearer <token>", extraemos solo el token
        const jwtToken = res.token.split(' ')[1]; // 'Bearer ' -> [0] es 'Bearer', [1] es el token
        localStorage.setItem('token', jwtToken);
        console.log('Token guardado:', jwtToken);
      })
    );
  }


}
