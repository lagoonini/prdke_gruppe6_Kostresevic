import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

interface Vehicle {
  id: number;
  // Add other vehicle properties as needed
}
interface Route {
  id: number;
  vehicle: Vehicle;
  vehicleName: string;
  vehicleType: string;
  startPoint: string;
  endPoint: string;
  startPointLatitude: number;
  startPointLongitude: number;
  endPointLatitude: number;
  endPointLongitude: number;
}

@Component({
  selector: 'app-route-list',
  templateUrl: './route-list.component.html',
  styleUrls: ['./route-list.component.scss']
})
export class RouteListComponent implements OnInit {
  routes: Route[] = [];

  constructor(
    private http: HttpClient,
    private router: Router,
  ) {}

  ngOnInit(): void {
    this.loadRoutes();
  }

  loadRoutes(): void {
    const providerId = localStorage.getItem('providerId');
    this.http.get<Route[]>(`http://localhost:8080/routes?providerId=${providerId}`).subscribe({
      next: (data) => this.routes = data,
      error: (error) => console.error(error)
    });
  }

  editRoute(id: number): void {
    this.router.navigate(['/editRoute', id]); // Adjust the route as needed
  }

  deleteRoute(id: number): void {
    const providerId = localStorage.getItem('providerId');
    if (confirm('Are you sure you want to delete this route?')) {
      this.http.delete(`http://localhost:8080/routes/${id}?providerId=${providerId}`).subscribe({
        next: () => {
          alert('Route deleted successfully!');
          this.loadRoutes(); // Refresh the list after deletion
        },
        error: (error) => console.error(error)
      });
    }
  }
}
