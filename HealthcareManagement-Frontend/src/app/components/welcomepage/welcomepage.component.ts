import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { LoginService } from 'src/app/services/login.service';

@Component({
  selector: 'app-welcomepage',
  templateUrl: './welcomepage.component.html',
  styleUrls: ['./welcomepage.component.css']
})
export class WelcomepageComponent implements OnInit {

  isLoggedIn = false;
  userRole = '';
  userName = '';

  constructor(
    private activatedRoute: ActivatedRoute,
    private _router: Router,
    private _loginService: LoginService
  ) { }

  ngOnInit(): void {
    this.isLoggedIn = this._loginService.isLoggedIn();
    this.userRole   = this._loginService.userType();
    this.userName   = localStorage.getItem('name') || '';
  }

  goToDashboard() {
    if (this.userRole === 'admin')       this._router.navigate(['/admindashboard']);
    else if (this.userRole === 'doctor') this._router.navigate(['/doctordashboard']);
    else                                  this._router.navigate(['/userdashboard']);
  }

  navigate() {
    this._router.navigate(['/login']);
  }
}
