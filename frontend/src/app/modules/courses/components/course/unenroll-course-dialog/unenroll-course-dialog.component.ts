import { HttpErrorResponse } from '@angular/common/http';
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
  studentIsEnabled!: boolean;
  user!: User;
  courseId!: string;
  errMsg!: string;

  constructor(
    private dialogRef: MatDialogRef<UnenrollCourseDialogComponent>,
    @Inject(MAT_DIALOG_DATA)
    private data: { studentIsEnabled: boolean; courseId: string; user: User },
    private courseService: CourseService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.studentIsEnabled = this.data.studentIsEnabled;
    this.courseId = this.data.courseId;
    this.user = this.data.user;
  }

  onDelete(userId: number) {
    if (this.studentIsEnabled) {
      this.deleteStudent(userId);
    } else {
      this.deleteTeacher(userId);
    }
  }

  private deleteStudent(studentId: number) {
    this.courseService
      .studentCourseUnEnrollment(this.courseId, studentId)
      .subscribe({
        next: () => {
          console.log('haha');
        },
        error: (err) => {
          this.errMsg = err;
        },
        complete: () => {
          window.location.reload();
        },
      });
  }

  private deleteTeacher(teacherId: number) {
    this.courseService
      .teacherCourseUnEnrollment(this.courseId, teacherId)
      .subscribe({
        next: () => {},
        error: (err: HttpErrorResponse) => {
          this.errMsg = err.error.message;
        },
        complete: () => {
          window.location.reload();
        },
      });
  }
}
