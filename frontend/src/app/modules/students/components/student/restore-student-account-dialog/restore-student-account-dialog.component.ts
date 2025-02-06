import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { User } from 'src/app/modules/core/models/user.model';
import { StudentService } from 'src/app/modules/core/services/student.service';

@Component({
  selector: 'app-restore-student-account-dialog',
  templateUrl: './restore-student-account-dialog.component.html',
  styleUrls: ['./restore-student-account-dialog.component.css'],
})
export class RestoreStudentAccountDialogComponent {
  student!: User;
  errorMessage = '';

  constructor(
    private dialogRef: MatDialogRef<RestoreStudentAccountDialogComponent>,
    @Inject(MAT_DIALOG_DATA) private data: { student: User },
    private studentService: StudentService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.student = this.data.student;
  }

  onRestore() {
    this.studentService.restoreStudentAccount(this.student.id).subscribe({
      next: () => {
        this.dialogRef.close();
        window.location.reload();
      },
      error: (err) => {
        this.errorMessage = err;
      },
    });
  }
}
