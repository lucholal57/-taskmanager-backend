import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../models/user';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environments';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private apiUrl = environment.apiUrl + 'users';

  constructor(private http: HttpClient) { }

  // Obtener todos los usuarios 
  getAllUsers(): Observable<User[]> {
    return this.http.get<User[]>(this.apiUrl);
  }

  // Obtener usuario por id
      getUserById(id: number): Observable<User> {
        return this.http.get<User>(`${this.apiUrl}/${id}`);
      }

  // Crear usuario
  createUser(user: Partial<User>): Observable<User> {
    return this.http.post<User>(this.apiUrl,user);
  }

  // Actualizar usuario por id
  updateUser(id: number, user: Partial<User>): Observable<User>{
    return this.http.put<User>(`${this.apiUrl}/${id}`,user);
  }

  // Eliminar un usuario
  deleteUser(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

}
