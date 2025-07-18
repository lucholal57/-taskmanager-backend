import { Injectable } from '@angular/core';
import swal, { SweetAlertIcon } from 'sweetalert2';
@Injectable({
  providedIn: 'root'
})
export class AlertService {

  constructor() { }

  // Modal generico para mostrar mensajes de alerta
  showAlert(
    title: string,
    text: string,
    icon: SweetAlertIcon = 'info',
    timer: number = 2000
  ) {
    swal.fire({
      title,
      text,
      icon,
      timer,
      showConfirmButton: false,
      timerProgressBar: true,
    });
  }

  // Exito
  success(message: string,title: string = 'Éxito') {
    this.showAlert(title, message, 'success');
  }

  // Error
  error(message: string, title: string = 'Error') {
    this.showAlert(title, message, 'error');
  }

  // Warning
  warning(message: string, title: string = 'Advertencia') {
    this.showAlert(title, message, 'warning');
  } 

  // Info
  info(message: string, title: string = 'Información') {
    this.showAlert(title, message, 'info');
  } 

  // Confirmation
  confirm(
    title: string,
    text: string,
    confirmButtonText: string = 'Aceptar',
    cancelButtonText: string = 'Cancelar'
  ): Promise<boolean> {
    return swal.fire({
      title,
      text,
      icon: 'question',
      showCancelButton: true,
      confirmButtonText,
      cancelButtonText,
    }).then(result => {
      return result.isConfirmed;
    });
  }

}
