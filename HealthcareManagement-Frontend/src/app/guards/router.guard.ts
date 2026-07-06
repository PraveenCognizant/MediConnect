import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { LoginService } from '../services/login.service';

/**
 * ═══════════════════════════════════════════════════════════════════════
 *                          RouterGuard (router.guard.ts)
 *  The BASE authentication guard. Protects ALL dashboard routes.
 * ═══════════════════════════════════════════════════════════════════════
 *
 * WHAT IS A CANACTIVATE GUARD?
 *  When Angular Router tries to navigate to a route, it first calls canActivate().
 *  If it returns true  → navigation proceeds normally.
 *  If it returns false → navigation is blocked (+ redirect happens here).
 *
 * WHICH ROUTES USE THIS GUARD? (see app-routing.module.ts)
 *  /userdashboard, /admindashboard, /doctordashboard, /doctorlist, /userlist,
 *  /patientlist, /approvalstatus
 *
 * LOGIC:
 *  loginService.isLoggedIn() checks: USER and ROLE both exist in localStorage.
 *  If true  → user is authenticated (ANY role) → allow.
 *  If false → no valid session → redirect to /login → return false.
 *
 * NOTE: This guard only checks "are you logged in?" (any role).
 *  Role-specific access is enforced by AdminGuard, DoctorGuard, UserGuard.
 */
@Injectable({
  providedIn: 'root'
})
export class RouterGuard implements CanActivate {
  
  constructor(private router: Router, private _service : LoginService) { }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    if (this._service.isLoggedIn()) {
      return true; // Session exists → proceed to the requested route
    }
    // No session → redirect to login page
    this.router.navigate(['login']);
    return false; // Block the original navigation
  }
  
}
