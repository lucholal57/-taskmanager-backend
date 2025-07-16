import { Component, Input, OnInit } from '@angular/core';
import { Task } from '../../models/task.model';
import { TaskService } from '../../services/task.service';
import { NgFor, NgIf } from '@angular/common';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-task-list',
  standalone: true,
  imports: [NgIf, NgFor],
  templateUrl: './task-list.component.html',
  styleUrl: './task-list.component.scss'
})
export class TaskListComponent implements OnInit {

  @Input() userId!: number; // Recibe el id del usuario desde el componente padre

  tasks: Task[] = [];
  errorMessage: string = '';

  constructor(private taskService: TaskService) { }

  ngOnInit(): void {
    if (this.userId) {
      this.getTasksByUserId();
    }
  }

  loadTasks(): void {
    this.taskService.getAllTasks().subscribe({
      next: (tasks) => this.tasks = tasks,
      complete: () => console.log('Tasks loaded successfully', this.tasks),
      error: (err) => console.error('Error loading tasks', err)
    });
  }

  getTasksByUserId(): void {
    this.taskService.getTasksByUserId(this.userId).subscribe({
      next: (tasks) => { this.tasks = tasks; },
      complete: () => console.log('Tasks for user loaded successfully', this.tasks),
      error: (err) => { console.error('Error loading tasks for user', err); this.errorMessage = 'Error loading tasks for user'; }
    });

  }

}
