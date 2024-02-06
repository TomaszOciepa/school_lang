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
  @Input() course!: Course;
  @Output() closeDialog = new EventEmitter<void>();

  postCourse: PostCourse = {} as PostCourse;

  observer: Observer<unknown> = {
    next: (course) => {
      if (this.editMode) {
        this.emitCLoseDialog();
      }
      console.log('Zapisano do bazy: ' + course);
      this.router.navigate(['/courses']);
    },
    error: (err) => {
      console.log(err);
    },
    complete: () => {},
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
          ],
        }
      ),
      lessonsNumber: new FormControl(
        this.editMode ? this.course.lessonsNumber : 0,
        {
          nonNullable: true,
          validators: [Validators.required],
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
    this.postCourse.name = this.courseForm.getRawValue().name;
    this.postCourse.status = this.courseForm.getRawValue().status;
    this.postCourse.startDate = this.parseDateToStringFormat(
      this.courseForm.getRawValue().startDate.toString()
    );
    this.postCourse.endDate = this.parseDateToStringFormat(
      this.courseForm.getRawValue().endDate.toString()
    );

    this.postCourse.participantsLimit =
      this.courseForm.getRawValue().participantsLimit;
    this.postCourse.lessonsNumber = this.courseForm.getRawValue().lessonsNumber;
  }

  private parseDateToStringFormat(date: string): string {
    return this.dateParser.parseDate(date);
  }

  emitCLoseDialog() {
    this.closeDialog.emit();
  }
}
