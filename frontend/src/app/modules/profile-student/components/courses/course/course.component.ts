import { HttpErrorResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Course } from 'src/app/modules/core/models/course.model';
import { CourseService } from 'src/app/modules/core/services/course.service';
import { LoadUserProfileService } from 'src/app/modules/core/services/load-user-profile.service';

@Component({
  selector: 'app-course',
  templateUrl: './course.component.html',
  styleUrls: ['./course.component.css'],
})
export class CourseComponent {
  id!: string;
  course!: Course;

  listUserId!: number[];
  errMsg!: string;
  role!: string;

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
      complete: () => {},
    });
  }
}
