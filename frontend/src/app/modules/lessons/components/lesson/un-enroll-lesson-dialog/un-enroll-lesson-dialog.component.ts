import { HttpErrorResponse } from '@angular/common/http';
import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { AttendanceList } from 'src/app/modules/core/models/lesson.model';
import { User } from 'src/app/modules/core/models/user.model';
import { LessonsService } from 'src/app/modules/core/services/lessons.service';

@Component({
  selector: 'app-un-enroll-lesson-dialog',
  templateUrl: './un-enroll-lesson-dialog.component.html',
  styleUrls: ['./un-enroll-lesson-dialog.component.css'],
})
export class UnEnrollLessonDialogComponent {
  student!: AttendanceList;
  lessonId!: string;
  errorMessage = '';

  constructor(
    private dialogRef: MatDialogRef<UnEnrollLessonDialogComponent>,
    @Inject(MAT_DIALOG_DATA)
    private data: { lessonId: string; student: AttendanceList },
    private lessonService: LessonsService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.lessonId = this.data.lessonId;
    this.student = this.data.student;
  }

  onDelete(userId: number) {
    this.lessonService.unEnrollStudentLesson(this.lessonId, userId).subscribe({
      next: () => {},
      error: (err: HttpErrorResponse) => {
        console.log(err);
      },
      complete: () => {
        this.closeDialog();
        window.location.reload();
      },
    });
  }

  closeDialog() {
    this.dialogRef.close();
  }
}
