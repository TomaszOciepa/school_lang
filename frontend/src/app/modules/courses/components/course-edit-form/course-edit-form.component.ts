import { HttpErrorResponse } from '@angular/common/http';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Observer } from 'rxjs';
import {
  Course,
  EditCourseForm,
  PostCourse,
} from 'src/app/modules/core/models/course.model';
import { CourseService } from 'src/app/modules/core/services/course.service';
import { DateParserService } from 'src/app/modules/core/services/date-parser.service';
import { FormsService } from 'src/app/modules/core/services/forms.service';

@Component({
  selector: 'app-course-edit-form',
  templateUrl: './course-edit-form.component.html',
  styleUrls: ['./course-edit-form.component.css'],
})
export class CourseEditFormComponent {
  @Input() course: Course = {} as Course;
  @Output() closeDialog = new EventEmitter<void>();

  observer: Observer<unknown> = {
    next: () => {},
    error: (err: HttpErrorResponse) => {
      this.errMsg = err.error.message;
      this.hideErrorMsg();
    },
    complete: () => {
      window.location.reload();
    },
  };

  courseForm!: FormGroup<EditCourseForm>;
  postCourse: PostCourse = {} as PostCourse;
  errMsg!: string;

  isLoggedIn = false;
  // userProfile: KeycloakProfile | null = null;
  isTeacher: boolean = false;
  teacherEmail!: string | undefined;
  teacherId!: number;
  // teacherList: User[] = [];

  constructor(
    private formService: FormsService,
    // private userProfileService: LoadUserProfileService,
    private courseService: CourseService,
    // private teacherService: TeacherService,
    private router: Router,
    private dateParser: DateParserService
  ) {}

  async ngOnInit(): Promise<void> {
    // this.loadUserProfile();
    // this.getTeachers();
    this.initForm();
  }

  // async loadUserProfile(): Promise<void> {
  //   await this.userProfileService.loadUserProfile();
  //   this.isLoggedIn = this.userProfileService.isLoggedIn;
  //   this.userProfile = this.userProfileService.userProfile;
  //   this.isTeacher = this.userProfileService.isTeacher;

  //   if (this.isTeacher) {
  //     this.teacherEmail = this.userProfile?.email;
  //     this.getTeacherByEmail();
  //   }
  // }

  getErrorMessage(control: FormControl) {
    return this.formService.getErrorMessage(control);
  }

  get controls() {
    return this.courseForm.controls;
  }

  private initForm() {
    this.courseForm = new FormGroup<EditCourseForm>({
      name: new FormControl<string>(this.course.name, {
        nonNullable: true,
        validators: [
          Validators.required,
          Validators.minLength(3),
          Validators.maxLength(50),
        ],
      }),

      participantsLimit: new FormControl(this.course.participantsLimit, {
        nonNullable: true,
        validators: [
          Validators.required,
          Validators.min(1),
          Validators.max(26),
          Validators.pattern('[1-9][0-9]*'),
        ],
      }),
      language: new FormControl(this.course.language, {
        nonNullable: true,
        validators: [Validators.required],
      }),
      lessonsLimit: new FormControl(this.course.lessonsLimit, {
        nonNullable: true,
        validators: [
          Validators.required,
          Validators.min(1),
          Validators.pattern('[1-9][0-9]*'),
        ],
      }),
      startDate: new FormControl(new Date(this.course.startDate), {
        nonNullable: true,
        validators: [Validators.required],
      }),
      endDate: new FormControl(new Date(this.course.endDate), {
        nonNullable: true,
        validators: [Validators.required],
      }),
    });
  }

  // private getTeachers() {
  //   this.teacherService.getTeachers('ACTIVE').subscribe({
  //     next: (result) => {
  //       this.teacherList = result;
  //     },
  //     error: (err) => {
  //       console.log(err);
  //     },
  //     complete: () => {},
  //   });
  // }

  onAddCourse() {
    this.generateCourseObj();

    this.courseService
      .patchCourse(this.course.id, this.postCourse)
      .subscribe(this.observer);
    return;
  }

  private generateCourseObj() {
    if (this.courseForm.get('name')?.dirty) {
      this.postCourse.name = this.courseForm.getRawValue().name;
    }

    if (this.courseForm.get('language')?.dirty) {
      this.postCourse.language = this.courseForm.getRawValue().language;
    }

    if (this.courseForm.get('startDate')?.dirty) {
      this.postCourse.startDate = this.parseDateToStringFormat(
        this.courseForm.getRawValue().startDate.toString()
      );
      this.postCourse.endDate = this.parseDateToStringFormat(
        this.courseForm.getRawValue().endDate.toString()
      );
    }

    if (this.courseForm.get('endDate')?.dirty) {
      this.postCourse.startDate = this.parseDateToStringFormat(
        this.courseForm.getRawValue().startDate.toString()
      );
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

  // private getTeacherByEmail() {
  //   this.teacherService.getTeacherByEmail(this.teacherEmail).subscribe({
  //     next: (result) => {
  //       this.teacherId = result.id;
  //     },
  //     error: (err) => {
  //       console.log(err);
  //     },
  //     complete: () => {},
  //   });
  // }
}
