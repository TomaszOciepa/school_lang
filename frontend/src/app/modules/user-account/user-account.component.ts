import { Component, OnInit } from '@angular/core';
import { LoadUserProfileService } from '../core/services/load-user-profile.service';
import { TeacherService } from '../core/services/teacher.service';

@Component({
  selector: 'app-user-account',
  templateUrl: './user-account.component.html',
  styleUrls: ['./user-account.component.css'],
})
export class UserAccountComponent implements OnInit {
  email?: string;
  firstName?: string;
  lastName?: string;

  constructor(
    private userProfileService: LoadUserProfileService,
    private teacherService: TeacherService
  ) {}

  async ngOnInit(): Promise<void> {
    this.loadUserProfile();
  }

  async loadUserProfile(): Promise<void> {
    await this.userProfileService.loadUserProfile();

    if (this.userProfileService.isLoggedIn && this.userProfileService.isAdmin) {
      this.email = this.userProfileService.userProfile?.email;
      this.firstName = this.userProfileService.userProfile?.firstName;
      this.lastName = this.userProfileService.userProfile?.lastName;
    }

    if (
      this.userProfileService.isLoggedIn &&
      this.userProfileService.isTeacher
    ) {
      this.getTeacher(this.userProfileService.userProfile?.email);
    }
  }

  getTeacher(email?: string) {
    this.teacherService.getTeacherByEmail(email).subscribe({
      next: (result) => {
        this.email = result.email;
        this.firstName = result.firstName;
        this.lastName = result.lastName;
      },
    });
  }
}
