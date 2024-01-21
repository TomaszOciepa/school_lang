import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { User } from 'src/app/modules/core/models/user.model';
import { CourseService } from 'src/app/modules/core/services/course.service';

@Component({
  selector: 'app-unenroll-course-dialog',
  templateUrl: './unenroll-course-dialog.component.html',
  styleUrls: ['./unenroll-course-dialog.component.css'],
})
export class UnenrollCourseDialogComponent {
  student!: User;
  courseId!: string;
  errorMessage = '';

  constructor(
    private dialogRef: MatDialogRef<UnenrollCourseDialogComponent>,
    @Inject(MAT_DIALOG_DATA) private data: { courseId: string; student: User },
    private courseService: CourseService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.courseId = this.data.courseId;
    this.student = this.data.student;
  }

  onDelete(courseId: string, studentId: number) {
    this.courseService
      .studentCourseUnEnrollment(courseId, studentId)
      .subscribe({
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
