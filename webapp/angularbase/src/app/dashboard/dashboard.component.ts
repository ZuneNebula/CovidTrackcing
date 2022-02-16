import { Component } from '@angular/core';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { Observable } from 'rxjs';
import { map, shareReplay } from 'rxjs/operators';
import { Router } from '@angular/router';
import { CookiesService } from '../services/cookies.service';
import { ApiService } from '../services/api.service';
import { User } from '../user';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent{

  isHandset$: Observable<boolean> = this.breakpointObserver.observe(Breakpoints.Handset)
    .pipe(
      map(result => result.matches),
      shareReplay()
    );

  constructor(private breakpointObserver: BreakpointObserver, private router:Router, private cookieService:CookiesService, private apiService:ApiService) {}

  cookieValue!:User;

  ngOnInit(){
    if (!this.cookieService.getAuth) {
      this.logout();
      this.router.navigateByUrl("/");
    }
    console.log("here");
    this.cookieValue = this.cookieService.login();
  }

  logout(){
    this.cookieService.deleteAuth();
    this.cookieService.isLoggedIn=false;
  }

}
