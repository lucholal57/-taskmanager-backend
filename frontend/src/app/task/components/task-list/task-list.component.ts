import { Component, Input, OnInit } from '@angular/core';
import { Task } from '../../models/task.model';
import { TaskService } from '../../services/task.service';
import { NgFor, NgIf } from '@angular/common';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';

@Component({
    selector: 'app-task-list',
    imports: [NgIf, NgFor],
    templateUrl: './task-list.component.html',
    styleUrl: './task-list.component.scss'
})
export class TaskListComponent implements OnInit {

  @Input() userId!: number; // Recibe el id del usuario desde el componente padre

  tasks: Task[] = [];
  errorMessage: string = '';

  constructor(private taskService: TaskService,
    private router: Router
  ) { }

  ngOnInit(): void {

    this.loadTasks();
  }

  // Carga todas las tareas
  loadTasks(): void {
    this.taskService.getAllTasks().subscribe({
      next: (tasks) => this.tasks = tasks,
      complete: () => console.log('Tareas cargadas correctamente', this.tasks),
      error: (err) => console.error('Error al cargar tareas', err)
    });
  }

  // Eliminar tarea 
  deleteTask(taskId: number): void {
    if (confirm('Esta seguro de eliminar esta tarea?')) {
      this.taskService.deleteTask(taskId).subscribe({
        next: () => { this.loadTasks(); },
        error: (err) => {
          console.error('Error al eliminar tarea', err);
          this.errorMessage = 'Error en proceso de eliminación';
        }
      });
    }
  }

  // Editar Tarea
  updateTask(taskId: number): void {
    this.router.navigate(['/tasks/edit', taskId]);
  }

  // Crear Tarea  
  createTask(): void {
    this.router.navigate(['/tasks/create']);
  } 


  /*
  getTasksByUserId(): void {
    this.taskService.getTasksByUserId(this.userId).subscribe({
      next: (tasks) => { this.tasks = tasks; },
      complete: () => console.log('Tasks for user loaded successfully', this.tasks),
      error: (err) => { console.error('Error loading tasks for user', err); this.errorMessage = 'Error loading tasks for user'; }
    });

  }
    */

}
