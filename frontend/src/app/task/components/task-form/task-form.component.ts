import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { TaskService } from '../../services/task.service';
import { Task } from '../../models/task.model';
import { NgFor, NgIf } from '@angular/common';

@Component({
    selector: 'app-task-form',
    imports: [ReactiveFormsModule, NgIf, NgFor],
    templateUrl: './task-form.component.html',
    styleUrl: './task-form.component.css'
})
export class TaskFormComponent implements OnInit {

  form!: FormGroup;
  isEditMode: boolean = false;
  taskId!: number;

  constructor(
    private fb: FormBuilder,
    private taskService: TaskService,
    private router: Router,
    private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.form = this.fb.group({
      tittle: ['', [Validators.required, Validators.minLength(3)]],
      description: [''],
      completed: [false]
    });

    this.route.paramMap.subscribe(params => {
      const id = params.get('id');
      if (id) {
        this.isEditMode = true;
        this.taskId = +id;
        this.loadTask(this.taskId);
      }
    });

  }

  loadTask(id: number): void {
    this.taskService.getTaskById(id).subscribe({
      next: (task) => {
        this.form.patchValue(task);
      },
      error: (err) => {
        console.error('Error al cargar la tarea', err);
      }
    });
  }

  onSubmit(): void {
    if (this.form.valid) return;

    const taskData: Task = this.form.value;

    if(this.isEditMode){
      this.taskService.updateTask(this.taskId, taskData).subscribe({
        next: () => {
          this.router.navigate(['/tasks']);
        }
      });
    } else {
      this.taskService.createTask(taskData).subscribe({
        next: () => {
          this.router.navigate(['/tasks']);
        },
        error: (err) => {
          console.error('Error al crear la tarea', err);
        }
      });
    }
  }

  cancel(): void {
    this.router.navigate(['/tasks']);
  }




}
