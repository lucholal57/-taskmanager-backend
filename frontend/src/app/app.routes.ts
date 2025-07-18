import { Routes } from '@angular/router';
import { authGuard } from './auth/guards/auth.guard';

export const routes: Routes = [
    {
        path: '', loadComponent: () => import('./sidebar/components/sidebar/sidebar.component').then(m => m.SidebarComponent),
        children: [
            { path: 'dashboard', loadComponent: () => import('./auth/components/dashboard/dashboard.component').then(m => m.DashboardComponent), canActivate: [authGuard] },
            { path: 'task', loadComponent: () => import('./task/components/task-list/task-list.component').then(m => m.TaskListComponent), canActivate: [authGuard] },
            { path: 'task/new', loadComponent: () => import('./task/components/task-form/task-form.component').then(m => m.TaskFormComponent), canActivate: [authGuard] },
            { path: 'task/edit/:id', loadComponent: () => import('./task/components/task-form/task-form.component').then(m => m.TaskFormComponent), canActivate: [authGuard] },

        ]
    },
    { path: 'login', loadComponent: () => import('./auth/components/login/login.component').then(m => m.LoginComponent) },
    { path: '**', redirectTo: 'login', pathMatch: 'full' },
];
