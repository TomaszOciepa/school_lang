import { HttpErrorResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Course } from 'src/app/modules/core/models/course.model';
import { User } from 'src/app/modules/core/models/user.model';
import { CourseService } from 'src/app/modules/core/services/course.service';
import { LoadUserProfileService } from 'src/app/modules/core/services/load-user-profile.service';
import { StudentService } from 'src/app/modules/core/services/student.service';

@Component({
  selector: 'app-enroll-to-course',
  templateUrl: './enroll-to-course.component.html',
  styleUrls: ['./enroll-to-course.component.css'],
})
export class EnrollToCourseComponent {
  courseId!: string;
  course: Course = {} as Course;
  studentEmail?: string;
  student: User = {} as User;
  isEnrollmentSuccessful: boolean = false;
  errMsg: boolean = false;
  button: boolean = true;

  constructor(
    private userProfileService: LoadUserProfileService,
    private courseService: CourseService,
    private studentService: StudentService,
    private route: ActivatedRoute
  ) {}

  async ngOnInit(): Promise<void> {
    this.route.params;
    this.route.params.subscribe((params) => {
      this.courseId = params['id'];
    });

    this.getCourseById();
    await this.loadUserProfile();
    this.getStudent(this.studentEmail);
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

  async loadUserProfile(): Promise<void> {
    await this.userProfileService.loadUserProfile();
    this.studentEmail = this.userProfileService.userProfile?.email;
  }

  private getStudent(email?: string) {
    this.studentService.getStudentByEmail(email).subscribe({
      next: (result) => {
        this.student = result;
      },
      error: (err: HttpErrorResponse) => {
        console.log(err);
      },
      complete: () => {},
    });
  }

  enrollToCourse() {
    this.courseService
      .assignStudentToCourse(this.courseId, this.student.id)
      .subscribe({
        next: (response) => {
          console.log('next: ' + response);
        },
        error: (err: HttpErrorResponse) => {
          if (err.error.message == 'Student already enrolled on this course') {
            this.errMsg = true;
            this.button = false;
          }
        },
        complete: () => {
          this.isEnrollmentSuccessful = true;
          this.button = false;
        },
      });
  }
}
