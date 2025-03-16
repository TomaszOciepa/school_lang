import { Injectable } from '@angular/core';
import {
  ActivatedRouteSnapshot,
  Router,
  RouterStateSnapshot,
} from '@angular/router';
import { KeycloakAuthGuard, KeycloakService } from 'keycloak-angular';

@Injectable({
  providedIn: 'root',
})
export class AuthGuard extends KeycloakAuthGuard {
  private userRole!: string;

  constructor(
    protected override readonly router: Router,
    protected readonly keycloak: KeycloakService
  ) {
    super(router, keycloak);
  }

  public async isAccessAllowed(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ) {
    // Force the user to log in if currently unauthenticated.
    if (!this.authenticated) {
      await this.keycloak.login({
        redirectUri: window.location.origin + state.url,
      });
    }

    // Get the roles required from the route.
    const requiredRoles = route.data['roles'];
    // console.log('wymagane role: ' + requiredRoles);

    // Allow the user to proceed if no additional roles are required to access the route.
    if (!Array.isArray(requiredRoles) || requiredRoles.length === 0) {
      return false;
    }

    // Allow the user to proceed if no additional roles are required to access the route.
    if (requiredRoles.every((role) => !role)) {
      return true;
    }

    // Get user roles from Keycloak.
    const userRoles = this.roles;

    // Check if any of the required roles match user roles.
    const hasRequiredRole = requiredRoles.some((role) =>
      userRoles.includes(role)
    );

    // If user has any required role, allow access.
    if (hasRequiredRole) {
      return true;
    }

    this.router.navigate(['/**']);
    return false;
  }
}
