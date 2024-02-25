import { HttpErrorResponse } from '@angular/common/http';
import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { CourseMembers } from 'src/app/modules/core/models/course.model';
import { User } from 'src/app/modules/core/models/user.model';
import { CourseService } from 'src/app/modules/core/services/course.service';

@Component({
  selector: 'app-restore-student-dialog',
  templateUrl: './restore-student-dialog.component.html',
  styleUrls: ['./restore-student-dialog.component.css'],
})
export class RestoreStudentDialogComponent {
  student!: CourseMembers;
  courseId!: string;
  errMsg!: string;

  constructor(
    private dialogRef: MatDialogRef<RestoreStudentDialogComponent>,
    @Inject(MAT_DIALOG_DATA)
    private data: { student: CourseMembers; courseId: string },
    private courseService: CourseService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.student = this.data.student;
    this.courseId = this.data.courseId;
  }

  restoreStudentToCourse() {
    this.courseService
      .restoreStudentToCourse(this.courseId, this.student.id)
      .subscribe({
        next: () => {
          this.dialogRef.close();
        },
        error: (err: HttpErrorResponse) => {
          this.errMsg = err.error.message;
        },
        complete: () => {
          this.dialogRef.close();
          window.location.reload();
        },
      });
  }
}
