import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { VehicleCreateComponent } from './vehicle-create/vehicle-create.component';
import {VehicleListComponent} from "./vehicle-list/vehicle-list.component";
import {VehicleEditComponent} from "./vehicle-edit/vehicle-edit.component";
import {RouteListComponent} from "./route-list/route-list.component";
import {RouteEditComponent} from "./route-edit/route-edit.component";

const routes: Routes = [
  { path: '', redirectTo: '/dashboard', pathMatch: 'full' }, // default route
  { path: 'createVehicle', component: VehicleCreateComponent },
  { path: 'listVehicle', component: VehicleListComponent },
  { path: 'editVehicle/:id', component: VehicleEditComponent},
  { path: 'listRoute', component: RouteListComponent},
  { path: 'editRoute/:id', component: RouteEditComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
