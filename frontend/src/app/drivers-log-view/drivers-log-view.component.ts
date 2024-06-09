import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';

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
  addresses: string[]; // Ensure this is included as per your backend structure
  invoiceStatus: any;
}

@Component({
  selector: 'app-drivers-log-view',
  templateUrl: './drivers-log-view.component.html',
  styleUrls: ['./drivers-log-view.component.scss']
})
export class DriversLogViewComponent implements OnInit {
  log$: Observable<DriversLog> = of(); // Initialized with an empty observable
  logId: number = 0; // Default value for logId

  constructor(private route: ActivatedRoute, private http: HttpClient) {}

  ngOnInit(): void {
    this.logId = +this.route.snapshot.params['id']; // Using the unary plus to convert to number
    this.loadLogDetails();
  }

  loadLogDetails(): void {
    if (this.logId) { // Only make the HTTP request if logId is not 0
      const providerId = localStorage.getItem('providerId');
      this.log$ = this.http.get<DriversLog>(`http://localhost:8080/drivers-log/${this.logId}?providerId=${providerId}`).pipe(
        map(log => {
          // Filter out the start and endpoint addresses from the addresses array
          log.addresses = log.addresses.filter(address => address !== log.route.startPoint && address !== log.route.endPoint);
          return log;
        })
      );
    }
  }
}
