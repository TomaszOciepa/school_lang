import { JsonPipe } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { OAuthService } from 'angular-oauth2-oidc';
import { JwksValidationHandler } from 'angular-oauth2-oidc-jwks';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
})
export class AppComponent implements OnInit {
  title = 'frontend';

  constructor(private oAuthService: OAuthService, private http: HttpClient) {
    this.configureOAuth();
  }
  ngOnInit(): void {}

  private configureOAuth() {
    this.oAuthService.configure({
      requireHttps: false,
      clientId: 'my-front-mango',
      redirectUri: window.location.origin,
      userinfoEndpoint:
        'http://localhost:8030/realms/mango/protocol/openid-connect/userinfo',
      postLogoutRedirectUri: 'http://localhost:4200/',
      responseType: 'code',
      scope: 'openid profile email',
      issuer: 'http://localhost:8030/realms/mango',
      tokenEndpoint:
        'http://localhost:8030/realms/mango/protocol/openid-connect/token',
    });
    this.oAuthService.tokenValidationHandler = new JwksValidationHandler();
    this.oAuthService.loadDiscoveryDocumentAndTryLogin();
    // this.oAuthService.loadDiscoveryDocumentAndLogin();
    // this.oAuthService.loadDiscoveryDocument();
  }

  token!: String;

  login() {
    this.oAuthService.initImplicitFlow();
  }

  loout() {
    this.oAuthService.logOut();
  }

  claims: any;

  getClaims() {
    console.log('Claims: ' + this.oAuthService.getIdentityClaims());

    this.claims = this.oAuthService.getIdentityClaims();
    console.log('moze zadziala: ' + this.claims.name);
  }

  getIdentity() {
    this.token = this.oAuthService.getAccessToken();
    console.log('token: ' + this.token);
  }

  getTeacher() {
    this.http
      .get('http://localhost:8092/teacher/all', {
        headers: {
          Authorization: `Bearer ${this.oAuthService.getAccessToken()}`,
        },
      })
      .subscribe((result) => {
        console.log('result: ' + JSON.stringify(result));
      });
  }
}
