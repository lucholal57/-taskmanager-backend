import { Component, OnInit } from '@angular/core';
import { User } from '../../../user/models/user';
import { UserService } from '../../../user/services/user.service';
import { Router } from '@angular/router';
import { NgIf } from '@angular/common';
import { TokenService } from '../../services/token.service';
import { TaskListComponent } from '../../../task/components/task-list/task-list.component';
import { TaskService } from '../../../task/services/task.service';

@Component({
    selector: 'app-dashboard',
    imports: [NgIf, TaskListComponent],
    templateUrl: './dashboard.component.html',
    styleUrl: './dashboard.component.scss'
})
export class DashboardComponent implements OnInit {

  user!: User;
  errorMessage: string = '';

  // Variables para las métricas de las tareas
  totalTasks: number = 0;
  completedTasks: number = 0;
  pendingTasks: number = 0;


  constructor(private userService: UserService,
    private router: Router,
    private tokenService: TokenService,
    private taskService: TaskService
  ) { }

 ngOnInit(): void {
  const userId = this.tokenService.getUserId();
  if (userId) {
    this.userService.getUserById(userId).subscribe({
      next: (data) => {
        this.user = data;

        // Cargar métricas
        this.taskService.getAllTasks().subscribe((tasks) => {
          this.totalTasks = tasks.length;
          this.completedTasks = tasks.filter((t) => t.completed).length;
          this.pendingTasks = this.totalTasks - this.completedTasks;
        });
      },
      error: (error) => {
        this.errorMessage = 'Error al obtener el usuario: ' + error.message;
        if (error.status === 401) {
          this.tokenService.celarToken();
          this.router.navigate(['/login']);
        }
      },
    });
  } else {
    this.router.navigate(['/login']);
  }
}

  // Método para cerrar sesión
  logout(): void {
    localStorage.removeItem('token');
    this.router.navigate(['/login']); 
  }



}
