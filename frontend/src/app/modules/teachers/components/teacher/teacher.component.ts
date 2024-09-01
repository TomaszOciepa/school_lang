import { Component, OnInit } from '@angular/core';
import { TeacherService } from 'src/app/modules/core/services/teacher.service';
import { User } from 'src/app/modules/core/models/user.model';
import { ActivatedRoute, Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { DeleteTeacherDialogComponent } from './delete-teacher-dialog/delete-teacher-dialog.component';
import { EditTeacherDialogComponent } from './edit-teacher-dialog/edit-teacher-dialog.component';
import { LoadUserProfileService } from 'src/app/modules/core/services/load-user-profile.service';
import { DropTeacherDialogComponent } from './drop-teacher-dialog/drop-teacher-dialog.component';
import { RestoreTeacherAccountDialogComponent } from './restore-teacher-account-dialog/restore-teacher-account-dialog.component';

@Component({
  selector: 'app-teacher-details',
  templateUrl: './teacher.component.html',
  styleUrls: ['./teacher.component.css'],
})
export class TeacherDetailsComponent implements OnInit {
  id!: number;
  teacher!: User;
  role!: string;

  constructor(
    private userProfileService: LoadUserProfileService,
    private teacherService: TeacherService,
    private route: ActivatedRoute,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.loadUserProfile();
    this.route.params;
    this.route.params.subscribe((params) => {
      this.id = params['id'];
    });

    this.getTeacher(this.id);
  }

  async loadUserProfile(): Promise<void> {
    await this.userProfileService.loadUserProfile();
    if (this.userProfileService.isAdmin) {
      this.role = 'ADMIN';
    }
  }

  getTeacher(id: number) {
    this.teacherService.getTeacherById(id).subscribe((response) => {
      this.teacher = response;
    });
  }

  openDialog() {
    const dialogRef = this.dialog.open(DeleteTeacherDialogComponent, {
      data: {
        teacher: this.teacher,
      },
    });
  }

  openDropDialog(deleteSound: HTMLAudioElement) {
    deleteSound.play();
    const dialogRef = this.dialog.open(DropTeacherDialogComponent, {
      data: {
        teacher: this.teacher,
      },
    });
  }

  openRestoreDialog() {
    const dialogRef = this.dialog.open(RestoreTeacherAccountDialogComponent, {
      data: {
        teacher: this.teacher,
      },
    });
  }

  openEditDialog() {
    console.log('techaer id: ' + this.teacher.id);
    const dialogRef = this.dialog.open(EditTeacherDialogComponent, {
      data: {
        teacher: this.teacher,
      },
      width: '600px',
      maxWidth: '600px',
    });
  }
}
