import { Component } from '@angular/core';
import { KeycloakService } from 'keycloak-angular';
import { KeycloakProfile } from 'keycloak-js';
import { User } from '../../../core/models/user.model';
import { TeacherService } from '../../../core/services/teacher.service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-teacher-courses',
  templateUrl: './teacher-courses.component.html',
  styleUrls: ['./teacher-courses.component.css'],
})
export class TeacherCoursesComponent {
  public isLoggedIn = false;
  public userProfile: KeycloakProfile | null = null;
  public user!: User;

  constructor(
    private readonly keycloak: KeycloakService,
    private teacherService: TeacherService
  ) {}

  async ngOnInit(): Promise<void> {
    this.isLogged();
  }

  private async isLogged() {
    this.isLoggedIn = await this.keycloak.isLoggedIn();

    if (this.isLoggedIn) {
      this.userProfile = await this.keycloak.loadUserProfile();

      this.teacherService
        .getTeacherByEmail(this.keycloak.getUsername())
        .subscribe({
          next: (result) => {
            this.user = result;
          },
          error: (err: HttpErrorResponse) => {
            console.log(err.error.message);
          },
          complete: () => {},
        });
    }
  }
}
