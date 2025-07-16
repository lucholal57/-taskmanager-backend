import type { HttpInterceptorFn } from '@angular/common/http';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const token = localStorage.getItem('token');
  if (token) {
    const cloned = req.clone({
      setHeaders: { Authorization: `Bearer ${token}` }
    });
    return next(cloned);
  }
  // Si no hay token, simplemente pasamos la solicitud sin modificarla
  // Esto es útil para solicitudes que no requieren autenticación
  console.warn('No token found, request will not be authenticated');
  // Puedes manejar esto de diferentes maneras, como redirigir al usuario a la página de login
  // o simplemente continuar con la solicitud sin autenticación.
  // Descomentar si quieres continuar con la solicitud sin autenticación       
  return next(req);
};
