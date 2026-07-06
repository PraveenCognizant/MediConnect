import { Component, OnInit, HostListener } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  loggedUser = '';
  currRole = '';
  title = '';
  dropdownOpen = false;

  constructor(private activatedRoute: ActivatedRoute, private _router: Router) { }

  ngOnInit(): void {
    this.loggedUser = (localStorage.getItem('loggedUser') || '').replace(/"/g, '');
    this.currRole = (localStorage.getItem('ROLE') || '').replace(/"/g, '');

    if (this.currRole === 'admin') {
      this.title = 'Admin Dashboard';
    } else if (this.currRole === 'doctor') {
      this.title = 'Doctor Dashboard';
    } else if (this.currRole === 'user') {
      this.title = 'User Dashboard';
    }
  }

  toggleDropdown(event: Event): void {
    event.stopPropagation();
    this.dropdownOpen = !this.dropdownOpen;
  }

  onNavClick(event: Event): void {
    // Clicks anywhere inside nav but outside the dropdown button close it
    this.dropdownOpen = false;
  }

  @HostListener('document:click')
  onDocumentClick(): void {
    this.dropdownOpen = false;
  }

  logout(): void {
    this.dropdownOpen = false;
    localStorage.clear();
    this._router.navigate(['/login']);
  }

  navigateHome(): void {
    this.dropdownOpen = false;
    if (this.currRole === 'admin') {
      this._router.navigate(['/admindashboard']);
    } else if (this.currRole === 'doctor') {
      this._router.navigate(['/doctordashboard']);
    } else if (this.currRole === 'user') {
      this._router.navigate(['/userdashboard']);
    }
  }
}
