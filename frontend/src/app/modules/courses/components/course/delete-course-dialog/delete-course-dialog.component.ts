import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { Course } from 'src/app/modules/core/models/course.model';
import { CourseService } from 'src/app/modules/core/services/course.service';

@Component({
  selector: 'app-delete-course-dialog',
  templateUrl: './delete-course-dialog.component.html',
  styleUrls: ['./delete-course-dialog.component.css'],
})
export class DeleteCourseDialogComponent {
  course!: Course;
  errorMessage = '';

  constructor(
    private dialogRef: MatDialogRef<DeleteCourseDialogComponent>,
    @Inject(MAT_DIALOG_DATA) private data: { course: Course },
    private courseService: CourseService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.course = this.data.course;
  }

  onDelete() {
    this.courseService.deleteCourseById(this.course.id).subscribe({
      next: () => {
        this.dialogRef.close();
        this.router.navigate(['/courses']);
      },
      error: (err) => {
        this.errorMessage = err;
      },
    });
  }
}
