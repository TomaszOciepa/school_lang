import { HttpErrorResponse } from '@angular/common/http';
import { Component, EventEmitter, Output } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { KeycloakProfile } from 'keycloak-js';
import { Observer } from 'rxjs';

import {
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
  teacherIdNumber!: number;
  teacherList: User[] = [];
  lessonPrice: number = 0;
  courseLessonsLimit: number = 0;
  teacherSharePercentage: number = 0;
  regex = /^(?!0(\.0+)?$)(\d+(\.\d+)?|\.\d+)$/;

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

    this.initForm();

    this.courseForm.get('pricePerLesson')?.valueChanges.subscribe((value) => {
      this.lessonPrice = value;
    });

    this.courseForm.get('lessonsLimit')?.valueChanges.subscribe((value) => {
      this.courseLessonsLimit = value;
    });

    this.courseForm
      .get('teacherSharePercentage')
      ?.valueChanges.subscribe((value) => {
        this.teacherSharePercentage = value;
      });
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

    if (!this.isTeacher) {
      this.getTeachers();
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
      pricePerLesson: new FormControl<number>(0, {
        nonNullable: true,
        validators: [
          Validators.required,
          Validators.minLength(1),
          Validators.maxLength(10),
          Validators.pattern('[1-9][0-9]*'),
        ],
      }),
      teacherSharePercentage: new FormControl<number>(0, {
        nonNullable: true,
        validators: [
          Validators.required,
          Validators.minLength(1),
          Validators.maxLength(10),
          Validators.pattern(this.regex),
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
      startDate: new FormControl(new Date(), {
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

      teacherId: new FormControl(
        this.isTeacher ? this.teacherIdNumber.toString() : '',
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),

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
    console.log('send form ' + JSON.stringify(this.postCourse));
    this.courseService.addCourse(this.postCourse).subscribe(this.observer);
  }

  private generatePostCourseObj() {
    if (this.courseForm.get('name')?.dirty) {
      this.postCourse.name = this.courseForm.getRawValue().name;
    }

    if (this.courseForm.get('pricePerLesson')?.dirty) {
      this.postCourse.pricePerLesson =
        this.courseForm.getRawValue().pricePerLesson;
    }

    if (this.courseForm.get('language')?.dirty) {
      this.postCourse.language = this.courseForm.getRawValue().language;
    }

    const selectedDate: Date = this.courseForm.getRawValue().startDate;

    // Tworzenie daty z ustawioną godziną na 12:00, aby uniknąć przesunięć stref czasowych
    const localDate = new Date(
      selectedDate.getFullYear(),
      selectedDate.getMonth(),
      selectedDate.getDate(),
      12,
      0,
      0
    );

    // Pobranie daty w formacie 'yyyy-MM-dd'

    // Zapis daty do obiektu
    this.postCourse.startDate = localDate;

    if (this.courseForm.get('participantsLimit')?.dirty) {
      this.postCourse.participantsLimit =
        this.courseForm.getRawValue().participantsLimit;
    }

    if (this.courseForm.get('teacherSharePercentage')?.dirty) {
      this.postCourse.teacherSharePercentage =
        this.courseForm.getRawValue().teacherSharePercentage;
    }

    if (this.courseForm.get('lessonsLimit')?.dirty) {
      this.postCourse.lessonsLimit = this.courseForm.getRawValue().lessonsLimit;
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

    if (this.courseForm.get('teacherId')?.dirty) {
      this.postCourse.teacherId = parseInt(
        this.courseForm.getRawValue().teacherId,
        10
      );
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
        this.teacherIdNumber = result.id;
        this.teacherList.push(result);
      },
      error: (err) => {
        console.log(err);
      },
      complete: () => {},
    });
  }
}
