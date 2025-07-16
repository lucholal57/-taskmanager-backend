import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';

import { MaterialModule } from '../../../material.module';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Auth } from '../../models/auth.model';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, MaterialModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush,
})

export class LoginComponent {
  loginForm: FormGroup;

  constructor(private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private snackBar: MatSnackBar
  ) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required]],
    });
  }

  onSubmit(): void {
    console.log('📡 onSubmit llamado');
    if (!this.loginForm.valid) {
      console.log('Login invalido:', this.loginForm.value);
      return;
    }

    const loginData: Auth.LoginRequest = this.loginForm.value;

    this.authService.login(loginData).subscribe({
      next: (res: Auth.LoginResponse) => {
        console.log('Login exitoso:', res);
        this.snackBar.open('!Binvenido!', 'Cerrar', {
          duration: 3000
        });
        this.router.navigate(['/dashboard']);
      },
      error: (err) => {
        console.error('Error en el login:', err);
        this.snackBar.open('Error al iniciar sesión, Verifica tus credenciales.', 'Cerrar', {
          duration: 3000
        });
      }

    });
  }


}
