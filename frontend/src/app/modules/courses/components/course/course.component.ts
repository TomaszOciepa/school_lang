import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Course } from 'src/app/modules/core/models/course.model';
import { CourseService } from 'src/app/modules/core/services/course.service';
import { EditCourseDialogComponent } from './edit-course-dialog/edit-course-dialog.component';
import { MatDialog } from '@angular/material/dialog';
import { DeleteCourseDialogComponent } from './delete-course-dialog/delete-course-dialog.component';

@Component({
  selector: 'app-course',
  templateUrl: './course.component.html',
  styleUrls: ['./course.component.css'],
})
export class CourseComponent {
  id!: string;
  course!: Course;

  constructor(
    private courseService: CourseService,
    private route: ActivatedRoute,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.route.params;
    this.route.params.subscribe((params) => {
      this.id = params['id'];
    });

    this.getCourse(this.id);
  }

  getCourse(id: string) {
    this.courseService.getCourseById(id).subscribe((response) => {
      this.course = response;
    });
  }

  openDialog() {
    const dialogRef = this.dialog.open(DeleteCourseDialogComponent, {
      data: {
        course: this.course,
      },
    });
  }

  openEditDialog() {
    const dialogRef = this.dialog.open(EditCourseDialogComponent, {
      data: {
        course: this.course,
      },
      width: '600px',
      maxWidth: '600px',
    });
  }
}
