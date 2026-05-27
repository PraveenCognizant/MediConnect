import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { LoginService } from '../services/login.service';

/**
 * ═══════════════════════════════════════════════════════════════════════
 *                         AdminGuard (admin.guard.ts)
 *  Protects routes that are EXCLUSIVE to the admin role.
 * ═══════════════════════════════════════════════════════════════════════
 *
 * WHICH ROUTES USE THIS GUARD? (see app-routing.module.ts)
 *  /addDoctor, /approvedoctors
 *
 * LOGIC:
 *  loginService.isAdminLoggedIn() checks:
 *   1. isLoggedIn() → USER and ROLE both exist in localStorage
 *   2. ROLE === 'admin'
 *  Both must be true. A doctor or user who is logged in still gets false here.
 *
 * WHAT HAPPENS ON FALSE:
 *  Redirects to /login (not to the admin dashboard).
 *  If a user tries to type /addDoctor in the URL bar:
 *   → AdminGuard.canActivate() runs
 *   → isAdminLoggedIn() = false (they have ROLE='user')
 *   → Navigate to /login → return false
 *
 * DIFFERENCE FROM BACKEND SECURITY:
 *  AdminGuard is CLIENT-SIDE only — it prevents navigation in the Angular app.
 *  Even if someone bypassed it, the backend /addDoctor endpoint requires
 *  a JWT with ROLE_ADMIN, so the API call would still get 403 Forbidden.
 *  Both layers protect together (defense in depth).
 */
@Injectable({
  providedIn: 'root'
})
export class AdminGuard implements CanActivate {

  constructor(private _router : Router, private _service : LoginService) {}
  
  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) 
  {
    if (this._service.isAdminLoggedIn()) 
    {
      return true; // Logged in as admin → allow access
    }
    this._router.navigate(['login']); // Not admin → redirect to login
    return false;
  }
  
}
