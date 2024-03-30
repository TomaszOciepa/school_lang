import { Component, OnInit } from '@angular/core';
import { KeycloakService } from 'keycloak-angular';
import { KeycloakProfile } from 'keycloak-js';
import { LoadUserProfileService } from '../../services/load-user-profile.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css'],
})
export class HeaderComponent implements OnInit {
  public isLoggedIn = false;
  public userProfile: KeycloakProfile | null = null;
  public isAdmin: boolean = false;
  public isTeacher: boolean = false;
  public isStudent: boolean = false;

  constructor(
    private readonly keycloak: KeycloakService,
    private userProfileService: LoadUserProfileService
  ) {}

  async ngOnInit(): Promise<void> {
    this.loadUserProfile();
  }

  async loadUserProfile(): Promise<void> {
    await this.userProfileService.loadUserProfile();
    this.isLoggedIn = this.userProfileService.isLoggedIn;
    this.userProfile = this.userProfileService.userProfile;
    this.isAdmin = this.userProfileService.isAdmin;
    this.isTeacher = this.userProfileService.isTeacher;
    this.isStudent = this.userProfileService.isStudent;
  }

  public login() {
    this.keycloak.login();
  }

  public logout() {
    this.keycloak.logout('http://localhost:4200/');
  }
}
