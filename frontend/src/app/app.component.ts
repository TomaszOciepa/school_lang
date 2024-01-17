import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { KeycloakService } from 'keycloak-angular';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent implements OnInit {
  title = 'frontend';
  // public isLoggedIn = false;
  // public userProfile: KeycloakProfile | null = null;

  constructor(
    private readonly keycloak: KeycloakService,
    private http: HttpClient
  ) {}

  public async ngOnInit() {
    // this.isLoggedIn = await this.keycloak.isLoggedIn();
    // if (this.isLoggedIn) {
    //   this.userProfile = await this.keycloak.loadUserProfile();
    // }
  }

  // public login() {
  //   this.keycloak.login();
  // }

  // public logout() {
  //   this.keycloak.logout();
  // }
}
