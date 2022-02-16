import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { CookiesService } from './cookies.service';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private cookieService:CookiesService, private router:Router) { }

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ):
    | boolean
    | UrlTree
    | Observable<boolean | UrlTree>
    | Promise<boolean | UrlTree> {
      return this.authenticateUser(state.url);
  }


  authenticateUser(url: string) {
    if (this.cookieService.isLogged()) {
      return this.cookieService.isLoggedIn;
    }
    this.cookieService.redirectUrl = url;
    return this.router.parseUrl('/');
  }
}
