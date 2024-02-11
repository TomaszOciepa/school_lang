import { HttpErrorResponse } from '@angular/common/http';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Observer } from 'rxjs';

import {
  Course,
  PostCourse,
  PostCourseForm,
} from 'src/app/modules/core/models/course.model';
import { CourseService } from 'src/app/modules/core/services/course.service';
import { DateParserService } from 'src/app/modules/core/services/date-parser.service';
import { FormsService } from 'src/app/modules/core/services/forms.service';

@Component({
  selector: 'app-course-form',
  templateUrl: './course-form.component.html',
  styleUrls: ['./course-form.component.css'],
})
export class CourseFormComponent {
  courseForm!: FormGroup<PostCourseForm>;
  @Input() editMode = false;
  @Input() course: Course = {} as Course;
  @Output() closeDialog = new EventEmitter<void>();

  postCourse: PostCourse = {} as PostCourse;
  errMsg!: string;

  observer: Observer<unknown> = {
    next: () => {
      if (this.editMode) {
        this.emitCLoseDialog();
      }
    },
    error: (err: HttpErrorResponse) => {
      this.errMsg = err.error.message;
      this.hideErrorMsg();
    },
    complete: () => {
      if (this.editMode) {
        window.location.reload();
      } else {
        this.router.navigate(['/courses']);
      }
    },
  };

  constructor(
    private formService: FormsService,
    private courseService: CourseService,
    private router: Router,
    private dateParser: DateParserService
  ) {}

  ngOnInit(): void {
    this.initForm();
  }

  get controls() {
    return this.courseForm.controls;
  }
  private hideErrorMsg() {
    setTimeout(() => {
      this.errMsg = '';
    }, 3000);
  }

  private initForm() {
    this.courseForm = new FormGroup<PostCourseForm>({
      name: new FormControl<string>(this.editMode ? this.course.name : '', {
        nonNullable: true,
        validators: [
          Validators.required,
          Validators.minLength(3),
          Validators.maxLength(50),
        ],
      }),
      status: new FormControl(this.editMode ? this.course.status : '', {
        nonNullable: true,
        validators: [Validators.required],
      }),
      participantsLimit: new FormControl(
        this.editMode ? this.course.participantsLimit : 0,
        {
          nonNullable: true,
          validators: [
            Validators.required,
            Validators.min(1),
            Validators.max(26),
            Validators.pattern('[1-9][0-9]*'),
          ],
        }
      ),
      lessonsNumber: new FormControl(
        this.editMode ? this.course.lessonsNumber : 0,
        {
          nonNullable: true,
          validators: [
            Validators.required,
            Validators.min(1),
            Validators.pattern('[1-9][0-9]*'),
          ],
        }
      ),
      startDate: new FormControl(
        this.editMode ? new Date(this.course.startDate) : new Date(),
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      endDate: new FormControl(
        this.editMode ? new Date(this.course.endDate) : new Date(),
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
    });
  }

  getErrorMessage(control: FormControl) {
    return this.formService.getErrorMessage(control);
  }

  onAddCourse() {
    this.generatePostCourseObj();

    if (this.editMode) {
      this.courseService
        .patchCourse(this.course.id, this.postCourse)
        .subscribe(this.observer);
      return;
    }

    this.courseService.addNewCourse(this.postCourse).subscribe(this.observer);
  }

  private generatePostCourseObj() {
    this.courseForm.get('name')?.dirty;
    if (this.courseForm.get('name')?.dirty) {
      this.postCourse.name = this.courseForm.getRawValue().name;
    }

    if (this.courseForm.get('status')?.dirty) {
      this.postCourse.status = this.courseForm.getRawValue().status;
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

    if (this.courseForm.get('lessonsNumber')?.dirty) {
      this.postCourse.lessonsNumber =
        this.courseForm.getRawValue().lessonsNumber;
    }

    console.log(this.postCourse);
  }

  private parseDateToStringFormat(date: string): string {
    return this.dateParser.parseDate(date);
  }

  emitCLoseDialog() {
    this.closeDialog.emit();
  }
}
