import { Injectable } from '@angular/core';
import { KeycloakService } from 'keycloak-angular';
import { KeycloakProfile } from 'keycloak-js';

@Injectable({
  providedIn: 'root',
})
export class LoadUserProfileService {
  public isLoggedIn = false;
  public userProfile: KeycloakProfile | null = null;
  public isAdmin: boolean = false;
  public isTeacher: boolean = false;
  public isStudent: boolean = false;

  constructor(private readonly keycloak: KeycloakService) {}

  public async loadUserProfile() {
    console.log(' w środku LoadUserProfileService');
    this.isLoggedIn = await this.keycloak.isLoggedIn();

    if (this.isLoggedIn) {
      this.userProfile = await this.keycloak.loadUserProfile();

      this.isAdmin = this.keycloak.isUserInRole('admin');
      this.isTeacher = this.keycloak.isUserInRole('teacher');
      this.isStudent = this.keycloak.isUserInRole('student');
    }
  }
}
