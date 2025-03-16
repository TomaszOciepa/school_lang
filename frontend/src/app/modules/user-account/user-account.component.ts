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

    if (this.userProfileService.isLoggedIn) {
      this.getTeacher(this.userProfileService.userProfile?.email);
    }
  }

  getTeacher(email?: string) {
    this.teacherService.getTeacherByEmail(email).subscribe({
      next: (result) => {
        this.user = result;
      },
    });
  }

  openEditDialog() {
    console.log('user id: ');
    const dialogRef = this.dialog.open(EditTeacherDialogComponent, {
      data: {
        teacher: this.user,
      },
      width: '600px',
      maxWidth: '600px',
    });
  }

  getStudentStatus(status: any): string {
    switch (status) {
      case 'ACTIVE':
        return 'Aktywne';
      case 'INACTIVE':
        return 'Niekatywne';
      case 'REMOVED':
        return 'Usuniete';
      default:
        return '...';
    }
  }
}
