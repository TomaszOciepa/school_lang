import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { User } from 'src/app/modules/core/models/user.model';
import { StudentService } from 'src/app/modules/core/services/student.service';

@Component({
  selector: 'app-drop-student-dialog',
  templateUrl: './drop-student-dialog.component.html',
  styleUrls: ['./drop-student-dialog.component.css'],
})
export class DropStudentDialogComponent {
  student!: User;
  errorMessage = '';

  constructor(
    private dialogRef: MatDialogRef<DropStudentDialogComponent>,
    @Inject(MAT_DIALOG_DATA) private data: { student: User },
    private studentService: StudentService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.student = this.data.student;
  }

  onDrop() {
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
