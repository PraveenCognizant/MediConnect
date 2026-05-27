import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-admindashboard',
  templateUrl: './admindashboard.component.html',
  styleUrls: ['./admindashboard.component.css']
})
export class AdmindashboardComponent implements OnInit {

  name = 'admin';
  gender = '';
  loggedUser = '';
  currRole = '';
  users : Observable<any[]> | undefined;
  doctors : Observable<any[]> | undefined;
  slots : Observable<any[]> | undefined;
  appointments : Observable<any[]> | undefined;
  prescriptions : Observable<any[]> | undefined;
  
  constructor(private _route : Router, private _service : UserService) { }

  ngOnInit(): void 
  {
    this.name = (localStorage.getItem('loggedUser') || '').replace(/"/g, '');

    this.gender = (localStorage.getItem('gender') || '').replace(/"/g, '');

    this.loggedUser = (localStorage.getItem('loggedUser') || '').replace(/"/g, '');

    this.currRole = (localStorage.getItem('ROLE') || '').replace(/"/g, '');

    this.users = this._service.getTotalUsers();
    this.doctors = this._service.getTotalDoctors();
    this.slots = this._service.getTotalSlots();
    this.appointments = this._service.getTotalAppointments();
    this.prescriptions = this._service.getTotalPrescriptions();


  }

}
