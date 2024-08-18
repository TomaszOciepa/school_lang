import { Component, OnInit } from '@angular/core';
import { LoadUserProfileService } from '../core/services/load-user-profile.service';
import { TeacherService } from '../core/services/teacher.service';
import { User } from '../core/models/user.model';
import { MatDialog } from '@angular/material/dialog';
import { EditTeacherDialogComponent } from '../teachers/components/teacher/edit-teacher-dialog/edit-teacher-dialog.component';

@Component({
  selector: 'app-user-account',
  templateUrl: './user-account.component.html',
  styleUrls: ['./user-account.component.css'],
})
export class UserAccountComponent implements OnInit {
  user: User = {} as User;
  // email?: string;
  // firstName?: string;
  // lastName?: string;

  constructor(
    private userProfileService: LoadUserProfileService,
    private teacherService: TeacherService,
    private dialog: MatDialog
  ) {}

  async ngOnInit(): Promise<void> {
    this.loadUserProfile();
  }

  async loadUserProfile(): Promise<void> {
    await this.userProfileService.loadUserProfile();

    if (this.userProfileService.isLoggedIn && this.userProfileService.isAdmin) {
      this.user.email = this.userProfileService.userProfile?.email ?? '';
      this.user.firstName =
        this.userProfileService.userProfile?.firstName ?? '';
      this.user.lastName = this.userProfileService.userProfile?.lastName ?? '';
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
        // this.user.email = result.email;
        // this.user.firstName = result.firstName;
        // this.user.lastName = result.lastName;
        this.user = result;
      },
    });
  }

  openEditDialog() {
    const dialogRef = this.dialog.open(EditTeacherDialogComponent, {
      data: {
        teacher: this.user,
      },
      width: '600px',
      maxWidth: '600px',
    });
  }
}
