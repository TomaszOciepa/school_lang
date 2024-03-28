import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { User } from 'src/app/modules/core/models/user.model';
import { LoadUserProfileService } from 'src/app/modules/core/services/load-user-profile.service';
import { StudentService } from 'src/app/modules/core/services/student.service';

@Component({
  selector: 'app-my-account',
  templateUrl: './my-account.component.html',
  styleUrls: ['./my-account.component.css'],
})
export class MyAccountComponent implements OnInit {
  student!: User;

  constructor(
    private userProfileService: LoadUserProfileService,
    private studentService: StudentService
  ) {}

  async ngOnInit(): Promise<void> {
    this.loadUserProfile();
  }

  async loadUserProfile(): Promise<void> {
    await this.userProfileService.loadUserProfile();

    if (
      this.userProfileService.isLoggedIn &&
      this.userProfileService.isStudent
    ) {
      this.getStudentByEmail();
    }
  }

  private getStudentByEmail() {
    this.studentService
      .getStudentByEmail(this.userProfileService.userProfile?.email)
      .subscribe({
        next: (response) => {
          this.student = response;
        },
        error: (err: HttpErrorResponse) => {
          console.log(err.error.message);
        },
        complete: () => {},
      });
  }
}
