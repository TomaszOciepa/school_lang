import { Component } from '@angular/core';
import { CourseService } from '../../core/services/course.service';
import { ActivatedRoute } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { Course } from '../../core/models/course.model';
import { LoadUserProfileService } from '../../core/services/load-user-profile.service';
import { StudentService } from '../../core/services/student.service';

@Component({
  selector: 'app-course-details',
  templateUrl: './course-details.component.html',
  styleUrls: ['./course-details.component.css'],
})
export class CourseDetailsComponent {
  courseId!: string;
  course!: Course;

  isLoggedIn = false;
  isStudent: boolean = false;
  isStudentEnrolled: boolean = false;
  courseIsFull: boolean = false;
  studentEmail?: string;
  studentId!: number;

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
    if (this.isLoggedIn) {
      this.getStudent(this.studentEmail);
    }
  }

  private getCourseById() {
    this.courseService.getCourseById(this.courseId).subscribe({
      next: (result) => {
        this.course = result;
        if (result.participantsLimit === result.participantsNumber) {
          this.courseIsFull = true;
        }
      },
      error: (err: HttpErrorResponse) => {
        console.log(err);
      },
      complete: () => {},
    });
  }

  async loadUserProfile(): Promise<void> {
    await this.userProfileService.loadUserProfile();
    this.isLoggedIn = this.userProfileService.isLoggedIn;
    this.isStudent = this.userProfileService.isStudent;
    this.studentEmail = this.userProfileService.userProfile?.email;
  }

  private getStudent(email?: string) {
    this.studentService.getStudentByEmail(email).subscribe({
      next: (result) => {
        this.studentId = result.id;
      },
      error: (err: HttpErrorResponse) => {
        console.log(err);
      },
      complete: () => {
        this.isStudentEnrolledInCourse();
      },
    });
  }

  private isStudentEnrolledInCourse() {
    this.courseService
      .isStudentEnrolledInCourse(this.course, this.studentId)
      .subscribe({
        next: (result) => {
          this.isStudentEnrolled = result;
        },
        error: (err: HttpErrorResponse) => {
          console.log(err);
        },
        complete: () => {},
      });
  }

  getLanguageName(language: string): string {
    switch (language) {
      case 'ENGLISH':
        return 'Angielskiego';
      case 'POLISH':
        return 'Polskiego';
      case 'GERMAN':
        return 'Niemieckiego';
      default:
        return 'Nieznany';
    }
  }
}
