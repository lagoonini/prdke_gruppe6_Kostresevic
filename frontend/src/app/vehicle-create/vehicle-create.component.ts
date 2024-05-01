import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { GeocodeService } from '../geocode-service/geocode.service'; // Ensure this path matches your project structure
import { firstValueFrom } from 'rxjs';


interface Vehicle {
  id?: any;
  vehicleType: any;
  vehicleName: any;
  seats: any;
  wheelchairAccessible: any;
  startPoint: any;
  endPoint: any;
  startPointLatitude?: any;
  startPointLongitude?: any;
  endPointLatitude?: any;
  endPointLongitude?: any;
}

@Component({
  selector: 'app-vehicle-form',
  templateUrl: './vehicle-create.component.html',
  styleUrls: ['./vehicle-create.component.scss']
})
export class VehicleCreateComponent implements OnInit {
  vehicles: Vehicle[] = [];
  selectedVehicle: Vehicle | null = null;

  // Form fields
  vehicleType: string = '';
  vehicleName: string = '';
  seats: number = 0;
  wheelchairAccessible: boolean = false;
  startPoint: string = '';
  endPoint: string = '';

  constructor(private http: HttpClient, private geocodeService: GeocodeService) {}

  ngOnInit() {
    this.loadVehicles();
  }

  loadVehicles(): void {
    this.http.get<Vehicle[]>('http://localhost:8080/vehicles/').subscribe({
      next: (data) => this.vehicles = data,
      error: (error) => console.error(error)
    });
  }

  selectVehicle(vehicle: Vehicle): void {
    this.selectedVehicle = vehicle;
    this.vehicleType = vehicle.vehicleType;
    this.vehicleName = vehicle.vehicleName;
    this.seats = vehicle.seats;
    this.wheelchairAccessible = vehicle.wheelchairAccessible;
    this.startPoint= vehicle.startPoint;
    this.endPoint = vehicle.endPoint;
  }

  async saveVehicle(): Promise<void> {
    console.log('saveVehicle called'); // Check if method is triggered
    try {
      console.log('Attempting to fetch coordinates'); // Check if execution reaches here
      const startPointCoords = await firstValueFrom(this.geocodeService.getCoordinates(this.startPoint));
      console.log('Start Point Coordinates:', startPointCoords); // Log fetched coordinates
      const endPointCoords = await firstValueFrom(this.geocodeService.getCoordinates(this.endPoint));
      console.log('End Point Coordinates:', endPointCoords); // Log fetched coordinates
      const vehicle: Vehicle = {
        id: this.selectedVehicle?.id,
        vehicleType: this.vehicleType,
        vehicleName: this.vehicleName,
        seats: this.seats,
        wheelchairAccessible: this.wheelchairAccessible,
        startPoint: this.startPoint,
        endPoint: this.endPoint,
        startPointLatitude: startPointCoords.latitude, // Set latitude
        startPointLongitude: startPointCoords.longitude, // Set longitude
        endPointLatitude: endPointCoords.latitude, // Set latitude
        endPointLongitude: endPointCoords.longitude, // Set longitude
      };

      if (vehicle.id) {
        this.http.put(`http://localhost:8080/vehicles/${vehicle.id}`, vehicle, {
          headers: new HttpHeaders({ 'Content-Type': 'application/json' })
        }).subscribe({
          next: () => {
            alert('Vehicle updated successfully!');
            this.resetForm();
          },
          error: (error) => console.error(error)
        });
      } else {
        this.http.post('http://localhost:8080/vehicles/', vehicle, {
          headers: new HttpHeaders({ 'Content-Type': 'application/json' })
        }).subscribe({
          next: () => {
            alert('Vehicle added successfully!');
            this.resetForm();
          },
          error: (error) => console.error(error)
        });
      }
    } catch (error) {
      console.error('Failed to fetch coordinates', error);
    }
  }

  deleteVehicle(id: any): void {
    if (confirm('Are you sure you want to delete this vehicle?')) {
      this.http.delete(`http://localhost:8080/vehicles/${id}`).subscribe({
        next: () => {
          alert('Vehicle deleted successfully!');
          this.resetForm();
        },
        error: (error) => console.error(error)
      });
    }
  }

  resetForm(): void {
    this.selectedVehicle = null;
    this.vehicleType = '';
    this.vehicleName = '';
    this.seats = 0;
    this.wheelchairAccessible = false;
    this.startPoint = '';
    this.endPoint = '';
    this.loadVehicles(); // Reload vehicles to reflect changes
  }
}
