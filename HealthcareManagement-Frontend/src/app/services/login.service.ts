import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Doctor } from '../models/doctor';
import { User } from '../models/user';
import { map } from "rxjs/operators";

/**
 * ═══════════════════════════════════════════════════════════════════════
 *                          login.service.ts
 *  The CENTRAL AUTH SERVICE — handles login for all 3 roles and manages
 *  session state via localStorage.
 * ═══════════════════════════════════════════════════════════════════════
 *
 * WHY localStorage?
 *  localStorage persists across page refreshes and browser tabs.
 *  When Angular app restarts (F5), it reads from localStorage to restore session.
 *  If we used a component variable instead, the session would vanish on refresh.
 *
 * WHAT IS STORED IN localStorage AFTER LOGIN:
 *  KEY       VALUE EXAMPLE
 *  TOKEN   → 'Bearer eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJqb2huQ...'  (JWT for API calls)
 *  USER    → 'john@test.com'             (email of logged-in person)
 *  ROLE    → 'user' / 'doctor' / 'admin' (determines which dashboard to show)
 *  name    → 'John Doe'                  (display name in the header)
 *
 * HOW AUTH INTERCEPTOR USES THIS:
 *  AuthInterceptor reads localStorage.getItem('TOKEN') and injects it as:
 *    Authorization: Bearer eyJhbGci...
 *  on EVERY outgoing HTTP request from Angular → backend sees the JWT.
 *
 * HOW ROUTE GUARDS USE THIS:
 *  RouterGuard:  calls isLoggedIn()      → checks USER + ROLE exist
 *  AdminGuard:   calls isAdminLoggedIn() → checks ROLE === 'admin'
 *  DoctorGuard:  calls isDoctorLoggedIn()→ checks ROLE === 'doctor'
 *  UserGuard:    calls isUserLoggedIn()  → checks ROLE === 'user'
 */

const NAV_URL = environment.apiURL; // 'http://localhost:8081' from environment.ts

@Injectable({
  providedIn: 'root' // Singleton — ONE instance shared across the whole app
})
export class LoginService {

  user = new User();
  doctor = new Doctor();

  constructor(private _http : HttpClient) { }

  /**
   * POST /loginuser with user credentials.
   *
   * Returns an Observable. The component subscribes to it:
   *   loginService.loginUserFromRemote(user).subscribe(data => { navigate... })
   *
   * .pipe(map(data => { ... })) runs BEFORE the component's subscribe() callback.
   * Inside map():
   *   1. Store USER (email) in localStorage
   *   2. Store ROLE = 'user' in localStorage
   *   3. Store TOKEN = 'Bearer ' + jwt in localStorage
   *      (Prefixed with 'Bearer ' so AuthInterceptor can attach it directly as header)
   * Then the original response (data) is passed through to the component's subscribe().
   */
  public loginUserFromRemote(user : User)
  {
    return this._http.post<any>(`${NAV_URL}/loginuser`, user).pipe(
      map(data => {
        localStorage.setItem('USER', user.email);  // Email for display + lookups
        localStorage.setItem('ROLE', 'user');       // Guard checks use this
        if (data && data.token) {
          // 'Bearer ' prefix is added HERE so AuthInterceptor just does:
          //   headers.set('Authorization', localStorage.getItem('TOKEN'))
          localStorage.setItem('TOKEN', `Bearer ${data.token}`);
        } else {
          localStorage.removeItem('TOKEN');
        }
        return data; // Passes full UserAuthResponse to component's subscribe()
      })
    );
  }

  /**
   * POST /logindoctor — same pattern as loginUserFromRemote.
   * ROLE stored as 'doctor' → DoctorGuard will allow access to doctor routes.
   */
  public loginDoctorFromRemote(doctor : Doctor)
  {
    return this._http.post<any>(`${NAV_URL}/logindoctor`, doctor).pipe(
      map(data => {
        localStorage.setItem('USER', doctor.email);
        localStorage.setItem('ROLE', 'doctor');
        if (data && data.token) {
          localStorage.setItem('TOKEN', `Bearer ${data.token}`);
        } else {
          localStorage.removeItem('TOKEN');
        }
        return data; // Passes full DoctorAuthResponse to component
      })
    );
  }

  /**
   * Returns true if BOTH 'USER' and 'ROLE' are present in localStorage.
   * Used by RouterGuard to protect ALL dashboard routes.
   * Returns false if localStorage was cleared (logged out).
   */
  isLoggedIn(): boolean {
    const user = localStorage.getItem('USER');
    const role = localStorage.getItem('ROLE');
    return !!user && user.length > 0 && !!role && role.length > 0;
  }

  /** Returns true only for patients. Used by UserGuard. */
  isUserLoggedIn(): boolean {
    return this.isLoggedIn() && (localStorage.getItem('ROLE') || '').toLowerCase() === 'user';
  }

  /** Returns true only for doctors. Used by DoctorGuard. */
  isDoctorLoggedIn(): boolean {
    return this.isLoggedIn() && (localStorage.getItem('ROLE') || '').toLowerCase() === 'doctor';
  }

  /** Returns true only for admins. Used by AdminGuard. */
  isAdminLoggedIn(): boolean {
    return this.isLoggedIn() && (localStorage.getItem('ROLE') || '').toLowerCase() === 'admin';
  }

  /** Returns the stored JWT string (e.g. 'Bearer eyJhbGci...'). Used by AuthInterceptor. */
  getAuthenticatedToken() {
    return localStorage.getItem('TOKEN');
  }

  /** Returns the logged-in user's email from localStorage. */
  getAuthenticatedUser() {
    return localStorage.getItem('USER');
  }

  /** Returns the current role ('user', 'doctor', or 'admin'). */
  userType() {
    return (localStorage.getItem('ROLE') || '').toLowerCase();
  }

  /**
   * POST /loginadmin — Admin login.
   *
   * Note: Admin form sends email + password as separate fields (not a model object),
   * so we accept them as plain strings and build a simple object for the request body.
   *
   * After successful login:
   *  - TOKEN stored as 'Bearer <jwt>'
   *  - ROLE stored as 'admin'
   *  - 'name' stored for display in admin header
   */
  public adminLoginFromRemote(email: string, password: string): Observable<any> {
    return this._http.post<any>(`${NAV_URL}/loginadmin`, { email, password }).pipe(
      map(data => {
        localStorage.setItem('USER', email);
        localStorage.setItem('ROLE', 'admin');
        if (data && data.token) {
          localStorage.setItem('TOKEN', `Bearer ${data.token}`);
          localStorage.setItem('name', data.name || 'Admin'); // Display name in header
        } else {
          localStorage.removeItem('TOKEN');
        }
        return data;
      })
    );
  }

}
