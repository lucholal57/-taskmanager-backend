import { inject } from '@angular/core';
import { Router, type CanActivateFn } from '@angular/router';

export const authGuard: CanActivateFn = (route, state) => {
  const router = inject(Router);
  const token = localStorage.getItem('token');

  if (token) {
    return true; // Hay token dejar pasar
  } else {
    router.navigate(['/login']) // No hay token, redirigir a login
    return false; // Bloquear acceso

  }
}
