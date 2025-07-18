import { NgFor } from '@angular/common';
import { AfterViewInit, Component, OnInit } from '@angular/core';
import { NavigationEnd, Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { createIcons, icons } from 'lucide';
import { filter } from 'rxjs/operators';
import { FormsModule } from '@angular/forms';


@Component({
    selector: 'app-sidebar',
    imports: [RouterOutlet, RouterLink, RouterLinkActive, NgFor, FormsModule],
    templateUrl: './sidebar.component.html',
    styleUrl: './sidebar.component.css'
})
export class SidebarComponent  implements AfterViewInit, OnInit {

  sidebarOpen = false;
  isMobileView = window.innerWidth < 768;
  darkMode = false;

  ngAfterViewInit() {
    createIcons({icons}); // Inicializa todos los íconos Lucide
  }

  constructor(private router: Router) { }

 ngOnInit() {
  // Preferencia previa
  const storedTheme = localStorage.getItem('darkMode');
  this.darkMode = storedTheme === 'true';
  this.updateBodyClass();

  window.addEventListener('resize', () => {
    this.isMobileView = window.innerWidth < 768;
  });

  this.router.events
    .pipe(filter((e): e is NavigationEnd => e instanceof NavigationEnd))
    .subscribe(() => {
      if (this.isMobileView) this.sidebarOpen = false;
    });
}

toggleDarkMode() {
  this.updateBodyClass();
  localStorage.setItem('darkMode', String(this.darkMode));
}

updateBodyClass() {
  if (this.darkMode) {
    document.documentElement.classList.add('dark');
  } else {
    document.documentElement.classList.remove('dark');
  }
}

  // Cerrar sesion
  logout(): void {
    localStorage.removeItem('token');
    this.router.navigate(['/login']);

}

toggleSidebar() {
  this.sidebarOpen = !this.sidebarOpen;
}

}
