
import { ChangeDetectionStrategy, Component } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';

import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { Auth } from '../../models/auth.model';
import { AlertService } from '../../../shared/services/alert.service';

@Component({
    selector: 'app-login',
    imports: [ReactiveFormsModule],
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.css'],
    changeDetection: ChangeDetectionStrategy.OnPush
})

export class LoginComponent {
  loginForm: FormGroup;
  isLoading = false;


  constructor(private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private alertService: AlertService
  ) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required]],
    });
  }

  onSubmit(): void {
    this.isLoading = true;
    console.log('📡 onSubmit llamado');
    if (!this.loginForm.valid) {
      console.log('Login invalido:', this.loginForm.value);
      return;
    }

    const loginData: Auth.LoginRequest = this.loginForm.value;

    this.authService.login(loginData).subscribe({
      next: (res: Auth.LoginResponse) => {
        this.alertService.success('Inicio de sesión exitoso');
        this.router.navigate(['/dashboard']);
      },
      error: (err) => {
        console.error('Error en el login:', err);
        this.alertService.error('Error al iniciar sesión. Por favor, verifica tus credenciales.');
      },
      complete: () => {
        this.isLoading = false;
        console.log('Proceso de login completado');
      },

    });
  }


}
