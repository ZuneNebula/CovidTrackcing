import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DashboardComponent } from './dashboard/dashboard.component';
import { HomeComponent } from './home/home.component';
import { MystatsComponent } from './mystats/mystats.component';
import { ProfileComponent } from './profile/profile.component';
import { AuthService } from './services/auth.service';
import { StatsComponent } from './stats/stats.component';

const routes: Routes = [
  {
    path:"",
    component:HomeComponent
  },
  {
    path:"users",
    component:DashboardComponent,
    canActivate:[AuthService],
    children:[
      {
        path:"profile",
        component:ProfileComponent
      },
      {
        path:"stats",
        component:StatsComponent
      },
      {
        path:"mystats",
        component:MystatsComponent
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {useHash: true})],
  exports: [RouterModule]
})
export class AppRoutingModule { }
