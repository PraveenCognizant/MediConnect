import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { LoginService } from '../services/login.service';

@Injectable({
  providedIn: 'root'
})
export class LoginGuard implements CanActivate {

  constructor(private router: Router, private _service: LoginService) {}

  canActivate(): boolean {
    if (this._service.isLoggedIn()) {
      const role = this._service.userType();
      if (role === 'admin') {
        this.router.navigate(['/admindashboard']);
      } else if (role === 'doctor') {
        this.router.navigate(['/doctordashboard']);
      } else {
        this.router.navigate(['/userdashboard']);
      }
      return false;
    }
    return true;
  }
}
