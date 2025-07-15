import { Routes } from '@angular/router';

export const routes: Routes = [
    { path: 'login', loadComponent: () => import('./auth/components/login/login.component').then(m => m.LoginComponent) },
    { path: 'dashboard', loadComponent: () => import('./auth/components/dashboard/dashboard.component').then(m => m.DashboardComponent) },
    { path: '**', redirectTo: 'login', pathMatch: 'full' },
];
