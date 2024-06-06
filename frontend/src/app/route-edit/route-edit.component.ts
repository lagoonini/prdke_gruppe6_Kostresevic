import { Component, OnInit, AfterViewInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';
import { GeocodeService } from '../geocode-service/geocode.service';
import * as L from 'leaflet';

interface Vehicle {
  id: number;
}
interface Route {
  id: number;
  vehicle: Vehicle;
  vehicleName: string;
  vehicleType: string;
  startPoint: string;
  endPoint: string;
  startPointLatitude?: number;
  startPointLongitude?: number;
  endPointLatitude?: number;
  endPointLongitude?: number;
}

@Component({
  selector: 'app-route-edit',
  templateUrl: './route-edit.component.html',
  styleUrls: ['./route-edit.component.scss']
})
export class RouteEditComponent implements OnInit, AfterViewInit {
  routeDetails: Route = {
    id: 0,
    vehicle: { id: 0 },
    vehicleName: '',
    vehicleType: '',
    startPoint: '',
    endPoint: '',
  };

  private map: any;

  constructor(
    private http: HttpClient,
    private route: ActivatedRoute,
    private router: Router,
    private geocodeService: GeocodeService
  ) {}

  async ngOnInit(): Promise<void> {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      const routeId = +id;
      const providerId = localStorage.getItem('providerId');
      this.routeDetails = await firstValueFrom(this.http.get<Route>(`http://localhost:8080/routes/${routeId}?providerId=${providerId}`));
      this.initMap();
    }
  }

  ngAfterViewInit(): void {
    this.initMap();
  }

  private initMap(): void {
    if (!this.routeDetails.startPointLatitude || !this.routeDetails.startPointLongitude) {
      console.error("Start point coordinates are missing.");
      return;
    }

    this.map = L.map('map').setView([this.routeDetails.startPointLatitude, this.routeDetails.startPointLongitude], 13);
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '© OpenStreetMap contributors'
    }).addTo(this.map);

    if (this.routeDetails.endPointLatitude && this.routeDetails.endPointLongitude) {
      this.drawRoute();
    }
  }

  private drawRoute(): void {
    if (!this.routeDetails.startPointLatitude || !this.routeDetails.startPointLongitude || !this.routeDetails.endPointLatitude || !this.routeDetails.endPointLongitude) {
      console.error('Start or end coordinates are missing');
      return;
    }

    const apiKey = '5b3ce3597851110001cf6248c1bc8ce508ea4d3e94b1128c7c3478be'; // Use your actual OpenRouteService API key
    const start = `${this.routeDetails.startPointLongitude},${this.routeDetails.startPointLatitude}`;
    const end = `${this.routeDetails.endPointLongitude},${this.routeDetails.endPointLatitude}`;
    const apiUrl = `https://api.openrouteservice.org/v2/directions/driving-car?api_key=${apiKey}&start=${start}&end=${end}`;

    this.http.get(apiUrl).subscribe({
      next: (response: any) => {
        // Extracting coordinates from the response
        const coordinates = response.features[0].geometry.coordinates;
        // Convert coordinates to the format Leaflet expects ([lat, lng])
        const latLngs = coordinates.map((coord: [number, number]) => L.latLng(coord[1], coord[0]));
        // Draw the polyline on the map
        const polyline = L.polyline(latLngs, { color: 'blue' }).addTo(this.map);
        // Adjust the map view to fit the route
        this.map.fitBounds(polyline.getBounds());

        // Define the custom icon
        const customIcon = L.icon({
          iconUrl: 'assets/map_pointer_icon.png', // Path to the icon image in your project's assets folder
          iconSize: [30, 40], // Size of the icon
          iconAnchor: [15, 20], // Point of the icon which will correspond to marker's location
          popupAnchor: [0, -20] // Point from which the popup should open relative to the iconAnchor
        });

        // Add markers with the custom icon for the start and end points
        const startPoint = latLngs[0];
        const endPoint = latLngs[latLngs.length - 1];

        L.marker(startPoint, { icon: customIcon }).addTo(this.map)
          .bindPopup('Startpunkt Adresse')

        L.marker(endPoint, { icon: customIcon }).addTo(this.map)
          .bindPopup('Endpunkt Adresse')
      },
      error: (error) => console.error('Error fetching route:', error) // Debug
    });
  }

  async saveRoute(): Promise<void> {
    try {
      // Assuming startPoint and endPoint inputs are addresses needing geocoding
      const startPointCoords = await firstValueFrom(this.geocodeService.getCoordinates(this.routeDetails.startPoint));
      const endPointCoords = await firstValueFrom(this.geocodeService.getCoordinates(this.routeDetails.endPoint));

      // Update the routeDetails object with new coordinates
      this.routeDetails.startPointLatitude = startPointCoords.latitude;
      this.routeDetails.startPointLongitude = startPointCoords.longitude;
      this.routeDetails.endPointLatitude = endPointCoords.latitude;
      this.routeDetails.endPointLongitude = endPointCoords.longitude;

      // Prepare the data for PUT request
      const updatedRouteDetails = {
        ...this.routeDetails,
        startPointLatitude: startPointCoords.latitude,
        startPointLongitude: startPointCoords.longitude,
        endPointLatitude: endPointCoords.latitude,
        endPointLongitude: endPointCoords.longitude
      };

      // Send a PUT request to update the route
      const providerId = localStorage.getItem('providerId');
      await firstValueFrom(this.http.put(`http://localhost:8080/routes/${this.routeDetails.id}?providerId=${providerId}`, updatedRouteDetails));

      // Optionally, if the backend doesn't automatically update the vehicle based on route changes,
      // you might need to send a separate update request for the vehicle.
      // This is hypothetical and depends on your backend implementation.

      alert('Route updated successfully!');
      this.router.navigate(['/listRoute']); // Adjust the navigation route as necessary
    } catch (error) {
      console.error('Failed to update route', error);
    }
  }

  goBack(): void {
    this.router.navigate(['/listRoute']); // Adjust as needed
  }
}
