import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { Appointment } from 'src/app/models/appointment';
import { Prescription } from 'src/app/models/prescription';
import { Doctor } from 'src/app/models/doctor';
import { Slots } from 'src/app/models/slots';
import { DoctorService } from 'src/app/services/doctor.service';

@Component({
  selector: 'app-patientlist',
  templateUrl: './patientlist.component.html',
  styleUrls: ['./patientlist.component.css']
})
export class PatientlistComponent implements OnInit {

  currRole = '';
  loggedUser = '';
  patients : Observable<Appointment[]> | undefined;
  slots : Observable<Slots[]> | undefined;
  responses : Observable<any> | undefined;
  http: any;

  // Map of patientname → Prescription; populated after doctor's prescriptions are loaded
  prescriptionMap: { [patientname: string]: Prescription } = {};

  constructor(private _service : DoctorService) { }

  // ngOnInit(): void
  // {
  //   this.loggedUser = JSON.stringify(localStorage.getItem('loggedUser')|| '{}');
  //   this.loggedUser = this.loggedUser.replace(/"/g, '');

  //   this.currRole = JSON.stringify(localStorage.getItem('ROLE')|| '{}'); 
  //   this.currRole = this.currRole.replace(/"/g, '');

  //   if(this.currRole === "user")
  //   {
  //     this.patients = this._service.getPatientListByDoctorEmail(this.loggedUser);
  //   }
  //   else
  //   {
  //     this.patients = this._service.getPatientList();
  //   }
  //   this.slots = this._service.getSlotDetails(this.loggedUser);
  // }
ngOnInit(): void {
  // Read the correct localStorage keys set by login.service.ts
  this.loggedUser = (localStorage.getItem('USER') || '').replace(/"/g, '');
  this.currRole   = (localStorage.getItem('ROLE') || '').replace(/"/g, '');

  if (this.currRole.toLowerCase() === 'doctor') {
    // Load only appointments belonging to this doctor
    this.patients = this._service.getPatientListByDoctorEmail(this.loggedUser);
    // Load all prescriptions this doctor has written and build a quick-lookup map
    this._service.getPrescriptionsByDoctorEmail(this.loggedUser).subscribe((list: Prescription[]) => {
      this.prescriptionMap = {};
      list.forEach(p => {
        // Key by patient name so the template can do prescriptionMap[patient.patientname]
        if (p.patientname) this.prescriptionMap[p.patientname] = p;
      });
    });
  } else {
    // Admin sees the full list across all doctors
    this.patients = this._service.getPatientList();
  }

  this.slots = this._service.getSlotDetails(this.loggedUser);
}
  // acceptRequest(slot : string)
  // {
  //   this.responses = this._service.acceptRequestForPatientApproval(slot);
  //   $("#acceptbtn").hide();
  //   $("#rejectbtn").hide();
  //   $("#acceptedbtn").show();
  //   $("#rejectedbtn").hide();
  // }
acceptRequest(patient: any) {
  this._service.acceptRequestForPatientApproval(patient.id)
    .subscribe({
      next: () => { patient.appointmentstatus = 'accept'; },
      error: () => { patient.appointmentstatus = 'accept'; }
    });
}

rejectRequest(patient: any) {
  this._service.rejectRequestForPatientApproval(patient.id)
    .subscribe({
      next: () => { patient.appointmentstatus = 'reject'; },
      error: () => { patient.appointmentstatus = 'reject'; }
    });
}

updateAppointmentStatus(patient: any, status: string) {
  this._service.updateAppointmentStatus(patient.id, status)
    .subscribe({
      next: () => { patient.appointmentstatus = status; },
      error: () => { patient.appointmentstatus = status; }
    });
}


}
