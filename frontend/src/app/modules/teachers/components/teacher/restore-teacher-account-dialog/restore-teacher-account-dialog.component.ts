import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { User } from 'src/app/modules/core/models/user.model';
import { TeacherService } from 'src/app/modules/core/services/teacher.service';

@Component({
  selector: 'app-restore-teacher-account-dialog',
  templateUrl: './restore-teacher-account-dialog.component.html',
  styleUrls: ['./restore-teacher-account-dialog.component.css'],
})
export class RestoreTeacherAccountDialogComponent implements OnInit {
  teacher!: User;
  errorMessage = '';

  constructor(
    private dialogRef: MatDialogRef<RestoreTeacherAccountDialogComponent>,
    @Inject(MAT_DIALOG_DATA) private data: { teacher: User },
    private teacherService: TeacherService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.teacher = this.data.teacher;
  }

  onRestore() {
    this.teacherService.restoreTeacherAccount(this.teacher.id).subscribe({
      next: () => {
        this.dialogRef.close();
        // this.router.navigate([`/teachers/${this.teacher.id}`]);
        window.location.reload();
      },
      error: (err) => {
        this.errorMessage = err;
      },
    });
  }
}
