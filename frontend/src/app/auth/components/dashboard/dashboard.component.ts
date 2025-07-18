import { Component, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { BaseChartDirective } from 'ng2-charts';
import { forkJoin } from 'rxjs';

// 🌟 Servicios y modelos
import { User } from '../../../user/models/user';
import { UserService } from '../../../user/services/user.service';
import { TokenService } from '../../services/token.service';
import { TaskService } from '../../../task/services/task.service';

// 📊 Chart.js
import { Chart, CategoryScale, LinearScale, BarController, BarElement, DoughnutController, ArcElement, PieController, Legend, Tooltip, ChartConfiguration, ChartType } from 'chart.js';

// ✅ Registrar componentes de Chart.js
Chart.register(
  CategoryScale, LinearScale,
  BarController, BarElement,
  DoughnutController, ArcElement,
  PieController, Legend, Tooltip
);

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, BaseChartDirective],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {

  @ViewChild(BaseChartDirective) chart?: BaseChartDirective;

  // 👤 Usuario actual
  user!: User;
  errorMessage = '';

  // 📦 Métricas generales
  totalTasks = 0;
  completedTasks = 0;
  pendingTasks = 0;

  // 📊 Tipos de gráficos
  barChartType: ChartType = 'bar';
  doughnutChartType: ChartType = 'doughnut';
  pieChartType: ChartType = 'pie';

  // 📊 Datos
  barChartData: ChartConfiguration['data'] = { labels: [], datasets: [] };
  doughnutChartData: ChartConfiguration['data'] = {
    labels: ['Completadas', 'Pendientes'],
    datasets: [{ data: [0, 0], backgroundColor: ['#4CAF50', '#F44336'] }]
  };
  pieChartData: ChartConfiguration['data'] = {
    labels: [],
    datasets: [{ data: [], backgroundColor: ['#42A5F5', '#66BB6A', '#FFA726', '#AB47BC', '#EC407A'] }]
  };

  // ⚙️ Opciones
  chartOptions: ChartConfiguration['options'] = {
    responsive: true,
    animation: { duration: 0 },
    plugins: { legend: { display: true }, tooltip: { enabled: true } }
  };

  constructor(
    private userService: UserService,
    private router: Router,
    private tokenService: TokenService,
    private taskService: TaskService
  ) {}

  ngOnInit(): void {
    const userId = this.tokenService.getUserId();
    if (!userId) {
      this.router.navigate(['/login']);
      return;
    }
    this.loadUser(userId);
    this.loadCharts();
  }

  // 👤 Cargar usuario logueado
  private loadUser(userId: number): void {
    this.userService.getUserById(userId).subscribe({
      next: user => this.user = user,
      error: err => this.handleError(err)
    });
  }

  // 📊 Cargar datos de gráficos
  private loadCharts(): void {
    forkJoin({
      tasks: this.taskService.getAllTasks(),
      users: this.userService.getAllUsers()
    }).subscribe(({ tasks, users }) => {
      this.setMetrics(tasks);
      this.setBarChartData(tasks);
      this.setDoughnutChartData();
      this.setPieChartData(tasks, users);
      this.chart?.update();
    });
  }

  // 📦 Actualizar métricas
  private setMetrics(tasks: any[]): void {
    this.totalTasks = tasks.length;
    this.completedTasks = tasks.filter(t => t.completed).length;
    this.pendingTasks = this.totalTasks - this.completedTasks;
  }

  // 📊 Datos BarChart
  private setBarChartData(tasks: any[]): void {
    const tasksByMonth = Array(12).fill(0);
    tasks.forEach(t => {
      if (t.createdAt) {
        const month = new Date(t.createdAt).getMonth();
        tasksByMonth[month]++;
      }
    });
    this.barChartData = {
      labels: ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio',
        'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'],
      datasets: [{
        label: 'Tareas por Mes',
        data: tasksByMonth,
        backgroundColor: '#42A5F5'
      }]
    };
  }

  // 🍩 Datos DoughnutChart
  private setDoughnutChartData(): void {
    this.doughnutChartData.datasets[0].data = [this.completedTasks, this.pendingTasks];
  }

  // 🥧 Datos PieChart
  private setPieChartData(tasks: any[], users: User[]): void {
    const userMap = new Map(users.map(u => [u.id, u.username]));
    const tasksByUser: { [name: string]: number } = {};
    tasks.forEach(t => {
      const username = userMap.get(t.userId) || `Usuario ${t.userId}`;
      tasksByUser[username] = (tasksByUser[username] || 0) + 1;
    });
    this.pieChartData = {
      labels: Object.keys(tasksByUser),
      datasets: [{
        data: Object.values(tasksByUser),
        backgroundColor: ['#42A5F5', '#66BB6A', '#FFA726', '#AB47BC', '#EC407A']
      }]
    };
  }

  logout(): void {
    localStorage.removeItem('token');
    this.router.navigate(['/login']);
  }

  private handleError(error: any): void {
    this.errorMessage = 'Error al obtener datos: ' + error.message;
    if (error.status === 401) {
      this.tokenService.celarToken();
      this.router.navigate(['/login']);
    }
  }
}
