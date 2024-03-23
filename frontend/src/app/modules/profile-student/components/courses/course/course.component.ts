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
  // students!: CourseMembers[];
  // teachers!: User[];

  listUserId!: number[];
  errMsg!: string;
  role!: string;

  constructor(
    private courseService: CourseService,
    private route: ActivatedRoute
  ) // private dialog: MatDialog,
  // private router: Router
  {}

  async ngOnInit(): Promise<void> {
    this.route.params;
    this.route.params.subscribe((params) => {
      this.id = params['id'];
    });
    this.getCourseById(this.id);
    // this.getCourseMembers(this.id);
    // this.getCourseTeachers(this.id);
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

  // getCourseMembers(courseId: string) {
  //   this.courseService.getCourseMembers(courseId).subscribe({
  //     next: (student) => {
  //       this.students = student;
  //     },
  //     error: (err: HttpErrorResponse) => {
  //       console.log(err.error.message);
  //     },
  //     complete: () => {},
  //   });
  // }

  // getCourseTeachers(courseId: string) {
  //   this.courseService.getCourseTeachers(courseId).subscribe({
  //     next: (teacher) => {
  //       this.teachers = teacher;
  //     },
  //     error: (err: HttpErrorResponse) => {
  //       console.log(err.error.message);
  //       this.hideErrorMsg();
  //     },
  //     complete: () => {},
  //   });
  // }
}
