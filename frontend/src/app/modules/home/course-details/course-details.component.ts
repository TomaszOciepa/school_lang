import { Component } from '@angular/core';
import { CourseService } from '../../core/services/course.service';
import { ActivatedRoute } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { Course } from '../../core/models/course.model';
import { User } from '../../core/models/user.model';

@Component({
  selector: 'app-course-details',
  templateUrl: './course-details.component.html',
  styleUrls: ['./course-details.component.css'],
})
export class CourseDetailsComponent {
  courseId!: string;
  course!: Course;
  teachers!: User[];

  constructor(
    private courseService: CourseService,
    private route: ActivatedRoute
  ) {}

  async ngOnInit(): Promise<void> {
    this.route.params;
    this.route.params.subscribe((params) => {
      this.courseId = params['id'];
    });

    this.getCourseById();
    this.getCourseTeachers();
  }

  private getCourseById() {
    this.courseService.getCourseById(this.courseId).subscribe({
      next: (result) => {
        this.course = result;
      },
      error: (err: HttpErrorResponse) => {
        console.log(err);
      },
      complete: () => {},
    });
  }

  getCourseTeachers() {
    this.courseService.getCourseTeachers(this.courseId).subscribe({
      next: (teacher) => {
        this.teachers = teacher;
      },
      error: (err: HttpErrorResponse) => {
        console.log(err);
      },
      complete: () => {},
    });
  }
}
