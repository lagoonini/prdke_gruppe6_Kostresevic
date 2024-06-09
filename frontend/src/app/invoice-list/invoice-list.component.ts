import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';

interface Vehicle {
  id: number;
}
interface Coordinate {
  latitude: number;
  longitude: number;
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
  selector: 'app-invoice-list',
  templateUrl: './invoice-list.component.html',
  styleUrls: ['./invoice-list.component.scss']
})
export class InvoiceListComponent implements OnInit {
  invoices: Invoice[] = [];

  constructor(private http: HttpClient, private router: Router) {}

  ngOnInit(): void {
    this.loadInvoices();
  }

  loadInvoices(): void {
    const providerId = localStorage.getItem('providerId');
    this.http.get<Invoice[]>(`http://localhost:8080/invoice/?providerId=${providerId}`).subscribe({
      next: (data) => this.invoices = data,
      error: (error) => console.error(error)
    });
  }

  viewInvoice(invoiceId: number): void {
    this.router.navigate(['/invoice-view', invoiceId]); // Make sure the route is configured in your routing module
  }
}
