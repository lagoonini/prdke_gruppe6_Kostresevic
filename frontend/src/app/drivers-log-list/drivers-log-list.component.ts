import { Component, OnInit } from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import { Router } from '@angular/router';

interface Vehicle {
  id: number;
}

interface Route {
  id: number;
  vehicle: Vehicle;
  startPoint: string;
  endPoint: string;
}

interface DriversLog {
  id: number;
  route: Route;
  invoiceStatus: any;
}

@Component({
  selector: 'app-drivers-log-list',
  templateUrl: './drivers-log-list.component.html',
  styleUrls: ['./drivers-log-list.component.scss']
})
export class DriversLogListComponent implements OnInit {
  logs: DriversLog[] = [];

  constructor(
    private http: HttpClient,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadLogs();
  }

  loadLogs(): void {
    const providerId = localStorage.getItem('providerId');
    this.http.get<DriversLog[]>(`http://localhost:8080/drivers-log/?providerId=${providerId}`).subscribe({
      next: (data) => this.logs = data,
      error: (error) => console.error(error)
    });
  }

  editLog(id: number): void {
    this.router.navigate(['/editDriversLog', id]); // Adjust the navigation route as needed
  }

  deleteLog(id: number): void {
    const providerId = localStorage.getItem('providerId');
    if (confirm('Are you sure you want to delete this log entry?')) {
      this.http.delete(`http://localhost:8080/drivers-log/${id}?providerId=${providerId}`).subscribe({
        next: () => {
          alert('Drivers log deleted successfully!');
          this.loadLogs(); // Refresh the list after deletion
        },
        error: (error) => console.error('Error deleting drivers log:', error)
      });
    }
  }

  viewLog(id: number): void {
    this.router.navigate(['/drivers-log-view', id]);
  }

  generateInvoice(id: number): void {
    const providerId = localStorage.getItem('providerId');
    this.http.post(`http://localhost:8080/drivers-log/${id}/generate-invoice?providerId=${providerId}`, {}).subscribe({
      next: () => {
        alert('Invoice generated successfully!');
        this.loadLogs(); // Refresh the list after invoice generation
      },
      error: (error) => console.error('Error generating invoice:', error)
    });
  }

  createMockDriversLogData(): void {
    const providerId = localStorage.getItem('providerId');
    this.http.post(`http://localhost:8080/drivers-log/mock?providerId=${providerId}`, {}).subscribe({
      next: (mockLogData) => {
        alert('Mock Drivers Log created successfully!');
        this.loadLogs(); // Refresh the list to include the new mock log
      },
      error: (error) => console.error('Error creating mock drivers log:', error)
    });
  }
}
