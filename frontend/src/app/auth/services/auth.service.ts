import { Injectable } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { Auth } from '../models/auth.model';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environments';



@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) { }


  // Metodo login que recibe un objeto Auth.LoginRequest y devuelve un Observable<Auth.LoginResponse> eso trae del backen solo un token es un DTO
  // El token se guarda en el localStorage para usarlo en las peticiones posteriores
  // El token se espera que sea un string con el formato
  login(data: Auth.LoginRequest): Observable<Auth.LoginResponse> {
    return this.http.post<Auth.LoginResponse>(this.apiUrl + 'auth/login', data).pipe(
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
