import { Injectable } from '@angular/core';
import { KeycloakService } from 'keycloak-angular';
import { LoadUserProfileService } from './load-user-profile.service';
import { StudentService } from './student.service';
import { FirstLoginStudentAccountService } from './first-login-student-account.service';

@Injectable({
  providedIn: 'root',
})
export class StudentAccountService {
  userId!: number;

  constructor(
    private readonly keycloak: KeycloakService,
    private userProfileService: LoadUserProfileService,
    private studentService: StudentService,
    private firstLoginStudentAccountService: FirstLoginStudentAccountService
  ) {}

  async loadUserProfile(): Promise<void> {
    await this.userProfileService.loadUserProfile();

    if (!this.userProfileService.isLoggedIn) {
      this.login();
    }

    if (this.userProfileService.isStudent) {
      await this.studentAccountIsExists();
    }
  }

  private async studentAccountIsExists(): Promise<void> {
    // try {
    const result = await this.studentService
      .getStudentByEmail(this.userProfileService.userProfile?.email)
      .toPromise();
    if (result && result.id) {
      this.userId = result.id;
    } else {
      console.log('Student not found.');
    }
    // } catch (error: any) {
    //   if (error?.error?.message === 'Student does not exists.') {
    //     await this.createStudentIfNotExists();
    //   } else {
    //     console.log(error);
    //   }
    // }
  }

  private async createStudentIfNotExists(): Promise<void> {
    // try {
    //   const userId = await this.firstLoginStudentAccountService
    //     .createNewStudent()
    //     .toPromise();
    //   this.userId = userId ?? 0;
    // } catch (error) {
    //   console.log(error);
    // }
  }

  private login(): void {
    this.keycloak.login();
  }
}
