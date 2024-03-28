import { HttpErrorResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Course } from 'src/app/modules/core/models/course.model';
import { User } from 'src/app/modules/core/models/user.model';
import { CourseService } from 'src/app/modules/core/services/course.service';

@Component({
  selector: 'app-course',
  templateUrl: './course.component.html',
  styleUrls: ['./course.component.css'],
})
export class CourseComponent {
  id!: string;
  course!: Course;

  listUserId!: number[];
  role!: string;
  teachers!: User[];

  constructor(
    private courseService: CourseService,
    private route: ActivatedRoute
  ) {}

  async ngOnInit(): Promise<void> {
    this.route.params;
    this.route.params.subscribe((params) => {
      this.id = params['id'];
    });
    this.getCourseById(this.id);
  }

  getCourseById(id: string) {
    this.courseService.getCourseById(id).subscribe({
      next: (response) => {
        this.course = response;
      },
      error: (err: HttpErrorResponse) => {
        console.log(err.error.message);
      },
      complete: () => {
        this.getCourseTeachers(this.id);
      },
    });
  }

  getCourseTeachers(courseId: string) {
    this.courseService.getCourseTeachers(courseId).subscribe({
      next: (teacher) => {
        this.teachers = teacher;
      },
      error: (err: HttpErrorResponse) => {
        console.log(err.error.message);
      },
      complete: () => {},
    });
  }
}
