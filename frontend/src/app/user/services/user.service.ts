import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../models/user';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environments';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) { }


  // Metodo para llamar al backend y traer solo un usuario por id para poder mostrar en el dashboard los datos viendo si es que tiene token lo traiga ya
  // que es una ruta protegida y mostrarlo se va a buscar solo uno por el id
  getUserById(id: number): Observable<User> {
    return this.http.get<User>(this.apiUrl + 'users/' + id);
  }

  getAllUsers(): Observable<User[]> {
    return this.http.get<User[]>(this.apiUrl + 'users');
  }


}
