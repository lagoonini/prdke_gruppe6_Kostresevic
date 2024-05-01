import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';

interface Route {
  id?: number;
  vehicleId?: number;
  vehicleName: string;
  vehicleType: string;
  startPoint: string;
  startPointLatitude?: number;
  startPointLongitude?: number;
  endPoint: string;
  endPointLatitude?: number;
  endPointLongitude?: number;
}

interface Vehicle {
  id?: number;
  vehicleType: string;
  vehicleName: string;
  seats: number;
  wheelchairAccessible: boolean;
  startPoint?: string;
  startPointLatitude?: number;
  startPointLongitude?: number;
  endPoint?: string;
  endPointLatitude?: number;
  endPointLongitude?: number;
}

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit {
  displayedColumnsRoutes: string[] = ['startPoint', 'endPoint', 'vehicleName', 'vehicleType'];
  dataSourceRoutes: Route[] = []; // Updated to use Route interface

  displayedColumnsVehicles: string[] = ['vehicleName', 'vehicleType', 'seats', 'wheelchairAccessible'];
  dataSourceVehicles: Vehicle[] = []; // Updated to use Vehicle interface

  constructor(private http: HttpClient) {}

  ngOnInit() {
    this.fetchAllRoutes();
    this.fetchAllVehicles();
  }

  fetchAllRoutes() {
    this.http.get<Route[]>('http://localhost:8080/routes').subscribe(data => {
      this.dataSourceRoutes = data;
    }, error => console.error('There was an error fetching the routes', error));
  }

  fetchAllVehicles() {
    this.http.get<Vehicle[]>('http://localhost:8080/vehicles').subscribe(data => {
      this.dataSourceVehicles = data;
    }, error => console.error('There was an error fetching the vehicles', error));
  }
}
