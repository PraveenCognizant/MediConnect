import { Component, OnInit } from '@angular/core';
import { Appointment } from 'src/app/models/appointment';
import { Slots } from 'src/app/models/slots';
import { DoctorService } from 'src/app/services/doctor.service';

@Component({
  selector: 'app-appointments',
  templateUrl: './appointments.component.html',
  styleUrls: ['./appointments.component.css']
})
export class AppointmentsComponent implements OnInit {

  loggedUser = '';
  currRole = '';
  appointments: Appointment[] = [];
  slots: Slots[] = [];
  actionLoading: { [id: number]: boolean } = {};
  actionMessage: { [id: number]: string } = {};

  constructor(private _service: DoctorService) { }

  ngOnInit(): void {
    // Use 'USER' key — this is what login.service.ts sets for all roles
    this.loggedUser = (localStorage.getItem('USER') || '').replace(/"/g, '');
    this.currRole = (localStorage.getItem('ROLE') || '').replace(/"/g, '');
    this.loadData();
  }

  loadData(): void {
    this._service.getPatientListByDoctorEmail(this.loggedUser).subscribe((data: Appointment[]) => {
      this.appointments = data;
    });
    this._service.getSlotDetails(this.loggedUser).subscribe((data: Slots[]) => {
      this.slots = data;
    });
  }

  acceptAppointment(id: number): void {
    this.actionLoading[id] = true;
    this._service.acceptRequestForPatientApproval(id).subscribe({
      next: () => {
        this.actionLoading[id] = false;
        this.actionMessage[id] = 'accepted';
        this.loadData();
      },
      error: () => { this.actionLoading[id] = false; }
    });
  }

  rejectAppointment(id: number): void {
    this.actionLoading[id] = true;
    this._service.rejectRequestForPatientApproval(id).subscribe({
      next: () => {
        this.actionLoading[id] = false;
        this.actionMessage[id] = 'rejected';
        this.loadData();
      },
      error: () => { this.actionLoading[id] = false; }
    });
  }
}
