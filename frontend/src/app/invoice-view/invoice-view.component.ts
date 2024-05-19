import {Component, OnInit, AfterViewInit, ViewChild, ElementRef} from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import {firstValueFrom, Observable, of} from 'rxjs';
import { tap } from 'rxjs/operators';
import * as L from 'leaflet';
import 'leaflet-routing-machine';
import { decode } from '@mapbox/polyline';
import { jsPDF } from 'jspdf';
import 'leaflet-simple-map-screenshoter';
import 'leaflet-easyprint';


interface Coordinate {
  latitude: number;
  longitude: number;
}

interface Vehicle {
  id: number;
}

interface Route {
  id: number;
  vehicle: Vehicle;
  startPoint: string;
  endPoint: string;
}

interface Invoice {
  invoiceId: number;
  createdDate: Date;
  distance: number;
  totalCost: number;
  route: Route;
  coordinates: Coordinate[];
}

@Component({
  selector: 'app-invoice-view',
  templateUrl: './invoice-view.component.html',
  styleUrls: ['./invoice-view.component.scss']
})
export class InvoiceViewComponent implements OnInit, AfterViewInit {
  @ViewChild('mapContainer', { static: false }) mapContainer!: ElementRef;
  invoiceId: number = 0;
  invoice$: Observable<Invoice> = of();
  invoiceDetails: Invoice | undefined;
  private map: L.Map | undefined;
  mapInitialized = false;  // Flag to check if the map is initialized


  constructor(private route: ActivatedRoute, private http: HttpClient) {}

  ngOnInit(): void {
    this.invoiceId = +this.route.snapshot.paramMap.get('id')!;
    this.loadInvoiceDetails();
  }

  ngAfterViewInit(): void {
    setTimeout(() => {
      if (this.mapContainer) {
        this.initMap();
        // any further operations that depend on mapContainer
      } else {
        console.error('mapContainer is not available');
      }
    }, 0);
  }

  loadInvoiceDetails(): void {
    if (this.invoiceId) { // Only make the HTTP request if invoiceId is not 0
      this.invoice$ = this.http.get<Invoice>(`http://localhost:8080/invoice/${this.invoiceId}`).pipe(
        tap(invoice => {
          this.invoiceDetails = invoice;
          console.log('Invoice details:', this.invoiceDetails);
          if (this.invoiceDetails) {
            this.initMap();
          }
        })
      );
      this.invoice$.subscribe();
    }
  }

  private initMap(): void {
    if (!this.invoiceDetails || this.invoiceDetails.coordinates.length === 0) {
      console.error("Invoice coordinates are missing.");
      return;
    }

    const startCoordinate = this.invoiceDetails.coordinates[0];
    setTimeout(() => {
      const mapElement = document.getElementById('map');
      if (mapElement) {
        this.map = L.map('map', {attributionControl: false,
          zoomControl: false,
          fadeAnimation: false,
          zoomAnimation: false}).setView([startCoordinate.latitude, startCoordinate.longitude], 13);
        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
          attribution: '© OpenStreetMap contributors'
        }).addTo(this.map);
        console.log('Map initialized:', this.map);

        this.drawRoute();
        // Adding EasyPrint control directly to the map
        const printControl = L.easyPrint({
          title: 'Print map',
          position: 'topleft',
          sizeModes: ['Current', 'A4Landscape', 'A4Portrait'],
          filename: 'myMap',
          exportOnly: true,
          hideControlContainer: true,
          hidden: true // Hide the control, use programmatically
        }).addTo(this.map);

        // Store the printControl for later use
        (this.map as any).easyPrintControl = printControl;
      } else {
        console.error("Map container not found.");
      }
    }, 200); // Delay increased to 100ms for more reliable initialization
  }

  private drawRoute(): void {
    this.mapInitialized = true;
    if (!this.invoiceDetails || !this.map || this.invoiceDetails.coordinates.length === 0) {
      console.error('Invoice coordinates or map instance are missing');
      return;
    }

    const apiKey = '5b3ce3597851110001cf6248c1bc8ce508ea4d3e94b1128c7c3478be'; // Use your actual OpenRouteService API key
    // Ensure the coordinates are in [longitude, latitude] format
    const coordinates = this.invoiceDetails.coordinates.map(coord => [coord.longitude, coord.latitude]);

    console.log('Coordinates sent to API:', coordinates);

    const headers = new HttpHeaders({
      'Accept': 'application/json, application/geo+json, application/gpx+xml, img/png; charset=utf-8',
      'Authorization': apiKey,
      'Content-Type': 'application/json; charset=utf-8'
    });

    const body = { coordinates: coordinates };

    this.http.post('https://api.openrouteservice.org/v2/directions/driving-car', body, { headers }).subscribe({
      next: (response: any) => {
        if (!this.map) {
          console.error('Map is not initialized');
          return;
        }

        // Debug response
        console.log('Route response:', response);

        // Verify response structure
        if (!response || !response.routes || response.routes.length === 0) {
          console.error('Invalid response format:', response);
          return;
        }

        // Extracting and decoding the polyline from the response
        const routeGeometry = response.routes[0].geometry;
        console.log('Encoded polyline:', routeGeometry);

        const routeCoordinates = decode(routeGeometry);
        console.log('Decoded route coordinates:', routeCoordinates);

        if (!routeCoordinates || routeCoordinates.length === 0) {
          console.error('No route coordinates found in response');
          return;
        }

        // Convert coordinates to the format Leaflet expects ([lat, lng])
        const latLngs = routeCoordinates.map((coord: [number, number]) => {
          const latLng = L.latLng(coord[0], coord[1]); // Swap the coordinates
          console.log('LatLng:', latLng);
          return latLng;
        });

        // Check for any invalid coordinates
        const validLatLngs = latLngs.filter(coord => {
          const isValid = coord.lat >= -90 && coord.lat <= 90 && coord.lng >= -180 && coord.lng <= 180;
          if (!isValid) {
            console.error('Invalid coordinate found:', coord);
          }
          return isValid;
        });

        if (validLatLngs.length === 0) {
          console.error('No valid coordinates to draw.');
          return;
        }

        console.log('Valid LatLngs:', validLatLngs);

        // Draw the polyline on the map
        const polyline = L.polyline(validLatLngs, { color: 'blue' }).addTo(this.map);
        console.log('Polyline added:', polyline);

        // Adjust the map view to fit the route
        this.map.fitBounds(polyline.getBounds());
        console.log('Map view adjusted to fit polyline:', this.map.getBounds());

        // Define the custom icon
        const customIcon = L.icon({
          iconUrl: 'assets/map_pointer_icon.png', // Path to the icon image in your project's assets folder
          iconSize: [30, 40], // Size of the icon
          iconAnchor: [15, 20], // Point of the icon which will correspond to marker's location
          popupAnchor: [0, -20] // Point from which the popup should open relative to the iconAnchor
        });


        // Log the coordinates for markers and decoded route for comparison
        if (this.invoiceDetails?.coordinates) {
          console.log('Coordinates for markers:', this.invoiceDetails.coordinates);
        }
        console.log('Decoded route coordinates:', routeCoordinates);

        // Add markers with the custom icon for each coordinate with a small offset
        this.invoiceDetails?.coordinates.forEach(async (coord, index) => {
          if (this.map) {
            const offset = 0.0001; // Small offset value
            const latitudeOffset = coord.latitude + (index % 2 === 0 ? offset : -offset);
            const longitudeOffset = coord.longitude + (index % 2 === 0 ? offset : -offset);

            let iconUrl = 'assets/map_pointer_icon.png'; // Default icon
            if (index % 3 === 1) {
              iconUrl = 'assets/map_pointer_icon_blue.png'; // Second icon
            } else if (index % 3 === 2) {
              iconUrl = 'assets/map_pointer_icon_green.png'; // Third icon
            }

            // Define the custom icon using the determined URL
            const customIcon = L.icon({
              iconUrl: iconUrl,
              iconSize: [40, 50],
              iconAnchor: [15, 20],
              popupAnchor: [0, -20]
            });

            const address = await this.getAddress(coord.latitude, coord.longitude);
            const marker = L.marker([latitudeOffset, longitudeOffset], { icon: customIcon })
              .addTo(this.map)
              .bindPopup(`Address: ${address}`);
            console.log('Marker added:', marker);
          }
        });
      },
      error: (error) => console.error('Error fetching route:', error) // Debug
    });
  }

  private async getAddress(latitude: number, longitude: number): Promise<string> {
    const url = `https://nominatim.openstreetmap.org/reverse?format=json&lat=${latitude}&lon=${longitude}`;
    try {
      const response = await firstValueFrom(this.http.get<any>(url));
      return response.display_name;
    } catch (error) {
      console.error('Error fetching address:', error);
      return 'Unknown location';
    }
  }

  public captureMap(): void {
    if (this.map && (this.map as any).easyPrintControl) {
      const printControl = (this.map as any).easyPrintControl;
      setTimeout(() => {
        printControl.printMap('CurrentSize', 'myMap');
      }, 1000); // Allow some time for the control to fully initialize
    } else {
      console.error('Map is not initialized or print control is missing');
    }
  }


}
