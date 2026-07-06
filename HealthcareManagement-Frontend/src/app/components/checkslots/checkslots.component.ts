import { Component, OnInit } from '@angular/core';
import { Slots } from 'src/app/models/slots';
import { DoctorService } from 'src/app/services/doctor.service';

@Component({
  selector: 'app-checkslots',
  templateUrl: './checkslots.component.html',
  styleUrls: ['./checkslots.component.css']
})
export class CheckslotsComponent implements OnInit {

  currRole = '';
  loggedUser = '';
  slots: Slots[] = [];

  constructor(private _service : DoctorService) { }

  ngOnInit(): void
  {
    this.loggedUser = (localStorage.getItem('loggedUser') || '').replace(/"/g, '');
    this.currRole = (localStorage.getItem('ROLE') || '').replace(/"/g, '');

    this._service.getSlotList().subscribe((data: Slots[]) => {
      // Only show slots that have a doctor name and at least one available slot
      this.slots = data.filter(s =>
        s.doctorname && s.doctorname.trim() !== '' &&
        s.specialization && s.specialization.trim() !== ''
      );
    });
  }

}
