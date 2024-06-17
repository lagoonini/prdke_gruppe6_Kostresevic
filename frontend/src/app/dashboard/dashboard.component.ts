import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';

interface TransportServiceProvider {
  id: number;
  companyName: string;
}

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  transportServiceProvider: TransportServiceProvider | null = null;

  constructor(
    private router: Router,
    private http: HttpClient
  ) {}

  ngOnInit(): void {
    this.loadTransportServiceProvider();
  }

  navigate(path: string) {
    this.router.navigate([path]);
  }

  loadTransportServiceProvider(): void {
    const providerId = localStorage.getItem('providerId');
    if (providerId) {
      this.http.get<TransportServiceProvider>(`http://localhost:8080/tsp/${providerId}`).subscribe({
        next: (data) => this.transportServiceProvider = data,
        error: (error) => console.error('Error fetching transport service provider details:', error)
      });
    } else {
      console.error('No provider ID found in local storage');
    }
  }
}
