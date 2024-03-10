import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Router } from '@angular/router';

import { User } from 'src/app/modules/core/models/user.model';
import { StudentService } from 'src/app/modules/core/services/student.service';

@Component({
  selector: 'app-delete-student-dialog',
  templateUrl: './delete-student-dialog.component.html',
  styleUrls: ['./delete-student-dialog.component.css'],
})
export class DeleteStudentDialogComponent {
  student!: User;
  errorMessage = '';

  constructor(
    private dialogRef: MatDialogRef<DeleteStudentDialogComponent>,
    @Inject(MAT_DIALOG_DATA) private data: { student: User },
    private studentService: StudentService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.student = this.data.student;
  }

  onDelete() {
    this.studentService.deleteStudentById(this.student.id).subscribe({
      next: () => {
        this.dialogRef.close();
        this.router.navigate(['/students']);
      },
      error: (err) => {
        this.errorMessage = err;
      },
    });
  }
}
