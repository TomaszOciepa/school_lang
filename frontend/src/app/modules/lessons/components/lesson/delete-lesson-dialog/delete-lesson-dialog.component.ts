import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { Lesson } from 'src/app/modules/core/models/lesson.model';
import { LessonsService } from 'src/app/modules/core/services/lessons.service';

@Component({
  selector: 'app-delete-lesson-dialog',
  templateUrl: './delete-lesson-dialog.component.html',
  styleUrls: ['./delete-lesson-dialog.component.css'],
})
export class DeleteLessonDialogComponent {
  lesson!: Lesson;
  errorMessage = '';

  constructor(
    private dialogRef: MatDialogRef<DeleteLessonDialogComponent>,
    @Inject(MAT_DIALOG_DATA) private data: { lesson: Lesson },
    private lessonService: LessonsService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.lesson = this.data.lesson;
  }

  onDelete() {
    this.lessonService.deleteLessonById(this.lesson.id).subscribe({
      next: () => {
        this.dialogRef.close();
      },
      error: (err) => {
        this.errorMessage = err;
      },
      complete: () => {
        if (this.lesson.courseId) {
          this.router.navigate(['/courses/' + this.lesson.courseId]);
        } else {
          this.router.navigate(['/lessons']);
        }
      },
    });
  }
}
