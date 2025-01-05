import { HttpErrorResponse } from '@angular/common/http';
import { Component, EventEmitter, Output } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { KeycloakProfile } from 'keycloak-js';
import { Observer } from 'rxjs';

import {
  EnrollemntInfo,
  PostCourse,
  PostCourseForm,
} from 'src/app/modules/core/models/course.model';
import { User } from 'src/app/modules/core/models/user.model';
import { CourseService } from 'src/app/modules/core/services/course.service';
import { DateParserService } from 'src/app/modules/core/services/date-parser.service';
import { FormsService } from 'src/app/modules/core/services/forms.service';
import { LoadUserProfileService } from 'src/app/modules/core/services/load-user-profile.service';
import { TeacherService } from 'src/app/modules/core/services/teacher.service';

@Component({
  selector: 'app-course-form',
  templateUrl: './course-form.component.html',
  styleUrls: ['./course-form.component.css'],
})
export class CourseFormComponent {
  @Output() closeDialog = new EventEmitter<void>();

  observer: Observer<unknown> = {
    next: () => {},
    error: (err: HttpErrorResponse) => {
      this.errMsg = err.error.message;
      this.hideErrorMsg();
    },
    complete: () => {
      this.router.navigate(['/courses']);
    },
  };

  courseForm!: FormGroup<PostCourseForm>;
  postCourse: PostCourse = {} as PostCourse;
  errMsg!: string;

  isLoggedIn = false;
  userProfile: KeycloakProfile | null = null;
  isTeacher: boolean = false;
  teacherEmail!: string | undefined;
  teacherId!: number;
  teacherList: User[] = [];

  constructor(
    private formService: FormsService,
    private userProfileService: LoadUserProfileService,
    private courseService: CourseService,
    private teacherService: TeacherService,
    private router: Router,
    private dateParser: DateParserService
  ) {}

  async ngOnInit(): Promise<void> {
    this.loadUserProfile();
    this.getTeachers();
    this.initForm();
  }

  async loadUserProfile(): Promise<void> {
    await this.userProfileService.loadUserProfile();
    this.isLoggedIn = this.userProfileService.isLoggedIn;
    this.userProfile = this.userProfileService.userProfile;
    this.isTeacher = this.userProfileService.isTeacher;

    if (this.isTeacher) {
      this.teacherEmail = this.userProfile?.email;
      this.getTeacherByEmail();
    }
  }

  getErrorMessage(control: FormControl) {
    return this.formService.getErrorMessage(control);
  }

  get controls() {
    return this.courseForm.controls;
  }

  private initForm() {
    this.courseForm = new FormGroup<PostCourseForm>({
      name: new FormControl<string>('', {
        nonNullable: true,
        validators: [
          Validators.required,
          Validators.minLength(3),
          Validators.maxLength(50),
        ],
      }),
      price: new FormControl<string>('', {
        nonNullable: true,
        validators: [
          Validators.required,
          Validators.minLength(1),
          Validators.maxLength(10),
        ],
      }),
      participantsLimit: new FormControl(0, {
        nonNullable: true,
        validators: [
          Validators.required,
          Validators.min(1),
          Validators.max(26),
          Validators.pattern('[1-9][0-9]*'),
        ],
      }),
      language: new FormControl('', {
        nonNullable: true,
        validators: [Validators.required],
      }),
      lessonsLimit: new FormControl(0, {
        nonNullable: true,
        validators: [
          Validators.required,
          Validators.min(1),
          Validators.pattern('[1-9][0-9]*'),
        ],
      }),
      startDate: new FormControl('', {
        nonNullable: true,
        validators: [Validators.required],
      }),
      endDate: new FormControl('', {
        nonNullable: true,
        validators: [Validators.required],
      }),
      timeRange: new FormControl<string>('', {
        nonNullable: true,
        validators: [Validators.required],
      }),
      lessonDuration: new FormControl('', {
        nonNullable: true,
        validators: [Validators.required],
      }),

      teacherId: new FormControl('', {
        nonNullable: true,
        validators: [Validators.required],
      }),

      lessonFrequency: new FormControl<string>('', {
        nonNullable: true,
        validators: [Validators.required],
      }),
    });
  }

  private getTeachers() {
    this.teacherService.getTeachers('ACTIVE').subscribe({
      next: (result) => {
        this.teacherList = result;
      },
      error: (err) => {
        console.log(err);
      },
      complete: () => {},
    });
  }

  onAddCourse() {
    this.generatePostCourseObj();
    this.generateLessons();
    this.courseService.addCourse(this.postCourse).subscribe(this.observer);
  }

  private generatePostCourseObj() {
    if (this.courseForm.get('name')?.dirty) {
      this.postCourse.name = this.courseForm.getRawValue().name;
    }

    if (this.courseForm.get('price')?.dirty) {
      this.postCourse.price = this.courseForm.getRawValue().price;
    }

    if (this.courseForm.get('language')?.dirty) {
      this.postCourse.language = this.courseForm.getRawValue().language;
    }

    if (this.courseForm.get('startDate')?.dirty) {
      this.postCourse.startDate = this.parseDateToStringFormat(
        this.courseForm.getRawValue().startDate.toString()
      );
    }

    if (this.courseForm.get('endDate')?.dirty) {
      this.postCourse.endDate = this.parseDateToStringFormat(
        this.courseForm.getRawValue().endDate.toString()
      );
    }

    if (this.courseForm.get('participantsLimit')?.dirty) {
      this.postCourse.participantsLimit =
        this.courseForm.getRawValue().participantsLimit;
    }

    if (this.courseForm.get('lessonsLimit')?.dirty) {
      this.postCourse.lessonsLimit = this.courseForm.getRawValue().lessonsLimit;
    }

    if (this.isTeacher) {
      const id = this.teacherId;
      const teacherInfo: EnrollemntInfo = { id: id };
      this.postCourse.courseTeachers = [];
      this.postCourse.courseTeachers.push(teacherInfo);
    }
  }

  generateLessons() {
    if (this.courseForm.get('timeRange')?.dirty) {
      this.postCourse.timeRange = this.courseForm.getRawValue().timeRange;
    }

    if (this.courseForm.get('lessonDuration')?.dirty) {
      this.postCourse.lessonDuration = parseInt(
        this.courseForm.getRawValue().lessonDuration,
        10
      );
    }

    if (!this.isTeacher) {
      if (this.courseForm.get('teacherId')?.dirty) {
        this.postCourse.teacherId = parseInt(
          this.courseForm.getRawValue().teacherId,
          10
        );
      }
    }

    if (this.courseForm.get('lessonFrequency')?.dirty) {
      this.postCourse.lessonFrequency =
        this.courseForm.getRawValue().lessonFrequency;
    }
  }

  emitCLoseDialog() {
    this.closeDialog.emit();
  }

  private parseDateToStringFormat(date: string): string {
    return this.dateParser.parseDate(date);
  }

  private hideErrorMsg() {
    setTimeout(() => {
      this.errMsg = '';
    }, 3000);
  }

  private getTeacherByEmail() {
    this.teacherService.getTeacherByEmail(this.teacherEmail).subscribe({
      next: (result) => {
        this.teacherId = result.id;
      },
      error: (err) => {
        console.log(err);
      },
      complete: () => {},
    });
  }
}
