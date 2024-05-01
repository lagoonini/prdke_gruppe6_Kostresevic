import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

interface GeocodeApiResponse {
  features: Feature[];
}

interface Feature {
  geometry: Geometry;
}

interface Geometry {
  coordinates: number[]; // Assuming [longitude, latitude]
}

@Injectable({
  providedIn: 'root'
})

export class GeocodeService {

  constructor(private http: HttpClient) {}

  getCoordinates(address: string): Observable<{latitude: number, longitude: number}> {
    const apiKey = '5b3ce3597851110001cf6248c1bc8ce508ea4d3e94b1128c7c3478be'; // Replace with your actual API key
    const url = `https://api.openrouteservice.org/geocode/search?api_key=${apiKey}&text=${encodeURIComponent(address)}`;
    return this.http.get<any>(url).pipe(
      map(response => {
        if (response.features.length > 0) {
          const coords = response.features[0].geometry.coordinates;
          return { latitude: coords[1], longitude: coords[0] };
        } else {
          throw new Error('No coordinates found for the given address.');
        }
      })
    );
  }
}
