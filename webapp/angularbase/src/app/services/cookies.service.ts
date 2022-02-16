import { Injectable } from '@angular/core';
import { CookieService } from 'ngx-cookie-service';
import { JwtHelperService } from '@auth0/angular-jwt';
import { User } from '../user';

@Injectable({
  providedIn: 'root',
})
export class CookiesService {
  currentUser: User = {
    userId: '',
    username: '',
    firstName: '',
    lastName: '',
    avatarUrl: '',
    email: '',
    password:''
  };

  helper = new JwtHelperService();
  isLoggedIn: boolean = false;
  redirectUrl: string = '';

  constructor(private cookie: CookieService) {}

  login(): any {
    const decodedToken = this.helper.decodeToken(this.getAuth());
    this.currentUser.userId = decodedToken.userId;
    this.currentUser.username = decodedToken.username;
    this.currentUser.firstName = decodedToken.firstName;
    this.currentUser.lastName = decodedToken.lastName;
    this.currentUser.email = decodedToken.email;
    this.currentUser.avatarUrl = decodedToken.avatar;
    return this.currentUser;
  }

  isLogged(): boolean {
    if (this.cookie.check('JWT-TOKEN')) {
      const decodedToken = this.helper.decodeToken(this.getAuth());
      if (decodedToken.userId) {
        this.isLoggedIn = true;
        return true;
      }
    }

    return false;
  }

  getAuth() {
    return this.cookie.get('JWT-TOKEN');
  }

  setAuth(value: string) {
    this.cookie.set('JWT-TOKEN', value);
  }

  deleteAuth() {
    this.cookie.delete('JWT-TOKEN');
  }
}
