import { Component, OnInit } from '@angular/core';
import { User } from '../../../user/models/user';
import { UserService } from '../../../user/services/user.service';
import { Router } from '@angular/router';
import { NgIf } from '@angular/common';
import { TokenService } from '../../services/token.service';
import { TaskListComponent } from '../../../task/components/task-list/task-list.component';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [NgIf,TaskListComponent],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.scss'
})
export class DashboardComponent implements OnInit {

  user!: User;
  errorMessage: string = '';


  constructor(private userService: UserService,
    private router: Router,
    private tokenService: TokenService
  ) { }

  ngOnInit(): void {
    const userId = this.tokenService.getUserId();
    console.log('User ID from token:', userId);
    if (userId) {
      this.userService.getUserById(userId).subscribe({
        next: (data) => {
          this.user = data;
          console.log('Usuario obtenido:', this.user);
        },
        error: (error) => {
          this.errorMessage = 'Error al obtener el usuario: ' + error.message;
          console.error(this.errorMessage);
          if (error.status === 401) {
            this.tokenService.celarToken(); // Limpiar el token si hay un error de autorización 
            this.router.navigate(['/login']);
          }
        }
      });
    } else {
      // no hay token, redirigir al login
      this.router.navigate(['/login']);
    }
  }

  // Método para cerrar sesión
  logout(): void {
    localStorage.removeItem('token');
    this.router.navigate(['/login']); 
  }



}
