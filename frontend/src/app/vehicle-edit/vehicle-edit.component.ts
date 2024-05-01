import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';
import { GeocodeService } from '../geocode-service/geocode.service';

interface Vehicle {
  id: number;
  vehicleType: string;
  vehicleName: string;
  seats: number;
  wheelchairAccessible: boolean;
  startPoint: string;
  endPoint: string;
  startPointLatitude?: any;
  startPointLongitude?: any;
  endPointLatitude?: any;
  endPointLongitude?: any;
}

@Component({
  selector: 'app-vehicle-edit',
  templateUrl: './vehicle-edit.component.html',
  styleUrls: ['./vehicle-edit.component.scss']
})
export class VehicleEditComponent implements OnInit {
  vehicle: Vehicle = {
    id: 0,
    vehicleType: '',
    vehicleName: '',
    seats: 0,
    wheelchairAccessible: false,
    startPoint: '',
    endPoint: ''
  };

  constructor(
    private http: HttpClient,
    private route: ActivatedRoute,
    private router: Router,
    private geocodeService: GeocodeService
  ) {}

  async ngOnInit(): Promise<void> {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      const vehicleId = +id;
      this.vehicle = await firstValueFrom(this.http.get<Vehicle>(`http://localhost:8080/vehicles/${vehicleId}`));
      console.log("Fetched vehicle data:", this.vehicle);

      // After fetching, initialize the map to use the newly set coordinates
    }
  }

  async saveVehicle(): Promise<void> {
    if (this.vehicle.startPoint && this.vehicle.endPoint) {
      try {
        // Fetch new coordinates for the start and end points
        const startPointCoords = await firstValueFrom(this.geocodeService.getCoordinates(this.vehicle.startPoint));
        const endPointCoords = await firstValueFrom(this.geocodeService.getCoordinates(this.vehicle.endPoint));

        // Update the vehicle object with new coordinates
        this.vehicle.startPointLatitude = startPointCoords.latitude;
        this.vehicle.startPointLongitude = startPointCoords.longitude;
        this.vehicle.endPointLatitude = endPointCoords.latitude;
        this.vehicle.endPointLongitude = endPointCoords.longitude;

        // Then proceed to update the vehicle as before
        if (this.vehicle.id) {
          await firstValueFrom(this.http.put(`http://localhost:8080/vehicles/${this.vehicle.id}`, this.vehicle));
          alert('Vehicle updated successfully!');
          this.router.navigate(['/listVehicle']); // Adjust the route as necessary
        }
      } catch (error) {
        console.error('Failed to update vehicle', error);
      }
    }
  }

  async deleteVehicle(): Promise<void> {
    const confirmed = confirm('Are you sure you want to delete this vehicle?');
    if (confirmed && this.vehicle.id) {
      try {
        await firstValueFrom(this.http.delete(`http://localhost:8080/vehicles/${this.vehicle.id}`));
        alert('Vehicle deleted successfully');
        this.router.navigate(['/vehicles']); // Adjust the route as necessary
      } catch (error) {
        console.error('There was an error deleting the vehicle', error);
      }
    }
  }

  goBack(): void {
    this.router.navigate(['/listVehicle']); // Or use Location.back() if you prefer
  }
}
