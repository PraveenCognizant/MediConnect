import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Doctor } from 'src/app/models/doctor';
import { User } from 'src/app/models/user';
import { DoctorService } from 'src/app/services/doctor.service';
import { RegistrationService } from 'src/app/services/registration.service';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css']
})
export class RegistrationComponent implements OnInit {

  user = new User();
  doctor = new Doctor();
  msg = ' ';
  confirmPassword = '';
  confirmDoctorPassword = '';
  confirmPasswordBlurred = false;
  confirmDoctorPasswordBlurred = false;
  activeTab: 'user' | 'doctor' = 'user';

  constructor(private _registrationService : RegistrationService, private _doctorService : DoctorService, private _router : Router) { }

  ngOnInit(): void {}

  switchTab(tab: 'user' | 'doctor') {
    this.activeTab = tab;
    this.msg = ' ';
    this.confirmPasswordBlurred = false;
    this.confirmDoctorPasswordBlurred = false;
  }

  registerUser()
  {
    if (this.user.password !== this.confirmPassword) {
      this.msg = 'Passwords do not match. Please re-enter.';
      return;
    }
    this._registrationService.registerUserFromRemote(this.user).subscribe(
      (data: any) => {
        console.log("Registration Success", data);

        // ── Auto-login: persist the AuthResponse into localStorage ──────────
        // Store JWT token so guards see the user as authenticated immediately
        if (data && data.token) {
          localStorage.setItem('TOKEN', `Bearer ${data.token}`);
        }
        localStorage.setItem('loggedUser', data.email || this.user.email);
        localStorage.setItem('USER',       data.email || this.user.email);
        localStorage.setItem('ROLE',       'user');
        localStorage.setItem('name',       data.name  || this.user.username);
        localStorage.setItem('gender',     data.gender || this.user.gender);
        localStorage.setItem('age',        data.age   || this.user.age);

        // Navigate straight to the dashboard — no login page needed
        this._router.navigate(['/userdashboard']);
      },
    error => {
      console.log("Registration Failed", error);
      if (error.status === 0) {
        this.msg = "Cannot connect to server. Check CORS/Security settings.";
      } else if (error.status === 403) {
        this.msg = "Registration blocked by security/CORS. Server returned 403.";
      } else if (error.error) {
        this.msg = error.error;
      } else {
        this.msg = "User with " + this.user.email + " already exists !!!";
      }
    }
    )
  }

  registerDoctor()
  {
    if (this.doctor.password !== this.confirmDoctorPassword) {
      this.msg = 'Passwords do not match. Please re-enter.';
      return;
    }
    this._registrationService.registerDoctorFromRemote(this.doctor).subscribe(
      (data: any) => {
        console.log("Registration Success", data);

        // ── Auto-login: persist the AuthResponse into localStorage ──────────
        // Store JWT token so guards see the doctor as authenticated immediately
        if (data && data.token) {
          localStorage.setItem('TOKEN', `Bearer ${data.token}`);
        }
        localStorage.setItem('loggedUser',  data.email        || this.doctor.email);
        localStorage.setItem('USER',        data.email        || this.doctor.email);
        localStorage.setItem('ROLE',        'doctor');
        localStorage.setItem('doctorname',  data.name         || this.doctor.doctorname);
        localStorage.setItem('name',        data.name         || this.doctor.doctorname);
        localStorage.setItem('gender',      data.gender       || this.doctor.gender);

        // Navigate straight to the doctor dashboard — no login page needed
        // Note: the doctor account will be in 'pending' status and must be
        // approved by admin before full access; the dashboard shows this status.
        this._router.navigate(['/doctordashboard']);
      },
      error => {
        console.log("Registration Failed", error);
        if (error.status === 0) {
          this.msg = "Cannot connect to server. Please ensure the backend is running.";
        } else if (error.error && typeof error.error === 'string') {
          this.msg = error.error;
        } else {
          this.msg = "Doctor with " + this.doctor.email + " already exists !!!";
        }
      }
    )
  }

}
