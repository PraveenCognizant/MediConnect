import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-userdashboard',
  templateUrl: './userdashboard.component.html',
  styleUrls: ['./userdashboard.component.css']
})
export class UserdashboardComponent implements OnInit {

  name = '';
  gender = '';
  loggedUser = '';
  currRole = '';
  doctors : Observable<any[]> | undefined;
  slots : Observable<any[]> | undefined;

  constructor(private _service : UserService) { }

  ngOnInit(): void 
  {
    this.name = (localStorage.getItem('loggedUser') || '').replace(/"/g, '');

    this.gender = (localStorage.getItem('gender') || '').replace(/"/g, '');

    this.loggedUser = (localStorage.getItem('loggedUser') || '').replace(/"/g, '');

    this.currRole = (localStorage.getItem('ROLE') || '').replace(/"/g, '');

    this.doctors = this._service.getTotalDoctors();
    this.slots = this._service.getTotalSlots();

    // menu toggle handled via Angular (see HTML)
  }

}
