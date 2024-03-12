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
  @Input() editMode = false;
  @Input() course: Course = {} as Course;
  @Output() closeDialog = new EventEmitter<void>();

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

  courseForm!: FormGroup<PostCourseForm>;
  postCourse: PostCourse = {} as PostCourse;
  errMsg!: string;

  constructor(
    private formService: FormsService,
    private courseService: CourseService,
    private router: Router,
    private dateParser: DateParserService
  ) {}

  ngOnInit(): void {
    this.initForm();
  }

  getErrorMessage(control: FormControl) {
    return this.formService.getErrorMessage(control);
  }

  get controls() {
    return this.courseForm.controls;
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
      lessonsLimit: new FormControl(
        this.editMode ? this.course.lessonsLimit : 0,
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
        this.editMode ? new Date(this.course.startDate) : '',
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      endDate: new FormControl(
        this.editMode ? new Date(this.course.endDate) : '',
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
    });
  }

  onAddCourse() {
    this.generatePostCourseObj();

    if (this.editMode) {
      this.courseService
        .patchCourse(this.course.id, this.postCourse)
        .subscribe(this.observer);
      return;
    }

    this.courseService.addCourse(this.postCourse).subscribe(this.observer);
  }

  private generatePostCourseObj() {
    if (this.courseForm.get('name')?.dirty) {
      this.postCourse.name = this.courseForm.getRawValue().name;
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
}
