import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { RouterModule, Routes } from '@angular/router';
import { CommonModule, DatePipe } from '@angular/common';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HeaderComponent } from './header/header.component';
import { VehicleCreateComponent } from './vehicle-create/vehicle-create.component';
import { HttpClientModule } from "@angular/common/http";
import { DashboardComponent } from './dashboard/dashboard.component';
import { VehicleEditComponent } from './vehicle-edit/vehicle-edit.component';
import { VehicleListComponent } from './vehicle-list/./vehicle-list.component';
import { RouteEditComponent } from './route-edit/route-edit.component';
import { RouteListComponent } from './route-list/route-list.component';
import { DriversLogListComponent } from './drivers-log-list/drivers-log-list.component';
import { MatTabsModule } from "@angular/material/tabs";
import { MatTableModule } from "@angular/material/table";
import { MatToolbarModule } from "@angular/material/toolbar";
import { DriversLogViewComponent } from './drivers-log-view/drivers-log-view.component';
import { InvoiceListComponent } from './invoice-list/invoice-list.component';
import { InvoiceViewComponent } from './invoice-view/invoice-view.component';
import { LoginComponent } from './login/login.component';



const routes: Routes = [
  {path: 'dashboard', component: DashboardComponent},
  {path: 'createVehicle', component: VehicleCreateComponent},
  {path: 'listVehicle', component: VehicleListComponent},
  {path: 'editVehicle/:id', component: VehicleEditComponent},
  {path: 'listRoute', component: RouteListComponent},
  {path: 'editRoute/:id', component: RouteEditComponent},
  {path: 'listDriverLogs', component: DriversLogListComponent},
  {path: 'drivers-log-view/:id', component: DriversLogViewComponent},
  {path: 'listInvoices', component: InvoiceListComponent },
  {path: 'invoice-view/:id', component: InvoiceViewComponent },
  {path: 'login', component: LoginComponent },
];

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    VehicleCreateComponent,
    DashboardComponent,
    VehicleEditComponent,
    VehicleListComponent,
    RouteEditComponent,
    RouteListComponent,
    DriversLogListComponent,
    DriversLogViewComponent,
    InvoiceListComponent,
    InvoiceViewComponent,
    LoginComponent,
  ],
  imports: [
    RouterModule.forRoot(routes),
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    CommonModule,
    MatTabsModule,
    MatTableModule,
    MatToolbarModule,
  ],
  providers: [
    DatePipe
  ],
  bootstrap: [AppComponent]
})
export class AppModule {

}
