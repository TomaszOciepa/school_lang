import { Component, OnInit } from '@angular/core';
import { KeycloakService } from 'keycloak-angular';
import { KeycloakProfile } from 'keycloak-js';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css'],
})
export class HeaderComponent implements OnInit {
  public isLoggedIn = false;
  public userProfile: KeycloakProfile | null = null;
  public isAdmin: boolean = false;
  public isUser: boolean = false;

  constructor(private readonly keycloak: KeycloakService) {}

  async ngOnInit(): Promise<void> {
    this.isLoggedIn = await this.keycloak.isLoggedIn();

    if (this.isLoggedIn) {
      this.userProfile = await this.keycloak.loadUserProfile();

      const isAdmin = this.keycloak.isUserInRole('admin');
      const isUser = this.keycloak.isUserInRole('user');

      if (isAdmin) {
        this.isAdmin = isAdmin;
      }

      if (isUser) {
        this.isUser = isUser;
      }
    }
  }

  public login() {
    this.keycloak.login();
  }

  public logout() {
    this.keycloak.logout('http://localhost:4200/');
  }
}
