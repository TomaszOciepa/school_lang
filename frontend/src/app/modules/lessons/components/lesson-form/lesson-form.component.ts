import { HttpErrorResponse } from '@angular/common/http';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Observer } from 'rxjs';
import { Course } from 'src/app/modules/core/models/course.model';
import {
  Lesson,
  PostLesson,
  PostLessonForm,
} from 'src/app/modules/core/models/lesson.model';
import { User } from 'src/app/modules/core/models/user.model';
import { CourseService } from 'src/app/modules/core/services/course.service';
import { DateParserService } from 'src/app/modules/core/services/date-parser.service';
import { FormsService } from 'src/app/modules/core/services/forms.service';
import { LessonsService } from 'src/app/modules/core/services/lessons.service';
import { TeacherService } from 'src/app/modules/core/services/teacher.service';

@Component({
  selector: 'app-lesson-form',
  templateUrl: './lesson-form.component.html',
  styleUrls: ['./lesson-form.component.css'],
})
export class LessonFormComponent {
  lessonForm!: FormGroup<PostLessonForm>;
  @Input() editMode = false;
  @Input() lesson: Lesson = {} as Lesson;
  @Output() closeDialog = new EventEmitter<void>();

  newLesson: PostLesson = {} as PostLesson;
  coursesList!: Course[];
  teacherList!: User[];
  startTimeStr: string = '';
  endTimeStr: string = '';

  courseId!: string | null;
  course!: Course;
  errMsg!: string;

  observer: Observer<unknown> = {
    next: () => {
      if (this.editMode) {
        this.emitCLoseDialog();
      }
    },
    error: (err: HttpErrorResponse) => {
      this.errMsg = err.error.message;
    },
    complete: () => {
      if (this.editMode) {
        window.location.reload();
      }
      if (this.courseId) {
        this.router.navigate(['/courses/' + this.courseId]);
      } else {
        this.router.navigate(['/lessons']);
      }
    },
  };

  constructor(
    private formService: FormsService,
    private lessonsService: LessonsService,
    private courseService: CourseService,
    private teacherService: TeacherService,
    private dateParser: DateParserService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.courseId = this.route.snapshot.paramMap.get('id');

    if (this.courseId == null) {
      this.getTeachers();
    }

    if (this.courseId !== null) {
      this.getCourseTeachers(this.courseId);
    }

    this.initForm();
    if (this.editMode) {
      this.startTimeStr = this.generateTimeStr(this.lesson.startDate);
      this.endTimeStr = this.generateTimeStr(this.lesson.endDate);
    }
  }

  private generateTimeStr(date: string): string {
    const startDate = new Date(date);
    const hours = ('0' + startDate.getHours()).slice(-2);
    const minutes = ('0' + startDate.getMinutes()).slice(-2);

    const timeString = `${hours}:${minutes}`;
    return timeString;
  }

  get controls() {
    return this.lessonForm.controls;
  }

  private initForm() {
    this.lessonForm = new FormGroup<PostLessonForm>({
      eventName: new FormControl<string>(
        this.editMode ? this.lesson.eventName : '',
        {
          nonNullable: true,
          validators: [
            Validators.required,
            Validators.minLength(3),
            Validators.maxLength(50),
          ],
        }
      ),
      startDate: new FormControl(
        this.editMode ? new Date(this.lesson.startDate) : new Date(),
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      startTime: new FormControl(this.editMode ? this.startTimeStr : '', {
        nonNullable: true,
        validators: [Validators.required],
      }),
      endTime: new FormControl(this.editMode ? this.endTimeStr : '', {
        nonNullable: true,
        validators: [Validators.required],
      }),

      teacherId: new FormControl(
        this.editMode ? this.lesson.teacherId.toString() : '',
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),

      description: new FormControl(
        this.editMode ? this.lesson.description : '',
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

  onAddLesson() {
    this.generatedLessonObj();

    if (this.editMode) {
      this.lessonsService
        .patchLesson(this.lesson.id, this.newLesson)
        .subscribe(this.observer);
      return;
    }
    this.lessonsService.addLesson(this.newLesson).subscribe(this.observer);
  }

  private generatedLessonObj() {
    if (this.lessonForm.get('eventName')?.dirty) {
      this.newLesson.eventName = this.lessonForm.getRawValue().eventName;
    }

    if (
      this.lessonForm.get('startDate')?.dirty ||
      this.lessonForm.get('startTime')?.dirty
    ) {
      this.newLesson.startDate = this.generateDateTime(
        this.lessonForm.getRawValue().startDate,
        this.lessonForm.getRawValue().startTime
      ).toString();

      this.newLesson.startDate = this.parseDateToStringFormat(
        this.newLesson.startDate
      );
    }

    if (
      this.lessonForm.get('startDate')?.dirty ||
      this.lessonForm.get('endTime')?.dirty
    ) {
      this.newLesson.endDate = this.generateDateTime(
        this.lessonForm.getRawValue().startDate,
        this.lessonForm.getRawValue().endTime
      ).toString();

      this.newLesson.endDate = this.parseDateToStringFormat(
        this.newLesson.endDate
      );
    }

    if (this.courseId != null) {
      this.newLesson.courseId = this.courseId;
    }

    if (this.lessonForm.get('teacherId')?.dirty) {
      this.newLesson.teacherId = parseInt(
        this.lessonForm.getRawValue().teacherId,
        10
      );
    }

    // if (this.lessonForm.get('status')?.dirty) {
    //   this.newLesson.status = this.lessonForm.getRawValue().status;
    // }

    if (this.lessonForm.get('description')?.dirty) {
      this.newLesson.description = this.lessonForm.getRawValue().description;
    }
  }

  emitCLoseDialog() {
    this.closeDialog.emit();
  }

  private generateDateTime(date: Date, startTime: string): Date {
    const parsedTime = this.parseDataTimeToString(startTime);
    const newDate = new Date(date);

    if (!parsedTime) {
      console.error('Invalid time format:', startTime);
      return date;
    }

    if (parsedTime.hours !== undefined && parsedTime.minutes !== undefined) {
      newDate.setHours(parsedTime.hours);
      newDate.setMinutes(parsedTime.minutes);
    } else {
      console.error('Invalid hours');
    }

    return newDate;
  }

  private parseDataTimeToString(
    timeString: string
  ): { hours: number; minutes: number } | null {
    const timeRegex = /^(\d{1,2}):(\d{2})$/;
    const match = timeRegex.exec(timeString);
    const error = 'Invalid time format: ';

    if (!match) {
      console.error(error, timeString);
      return null;
    }

    const hours = parseInt(match[1], 10);
    const minutes = parseInt(match[2], 10);

    if (isNaN(hours) || isNaN(minutes)) {
      console.error(error, timeString);
      return null;
    }

    return { hours, minutes };
  }

  private parseDateToStringFormat(date: string): string {
    return this.dateParser.parseDate(date);
  }

  private getTeachers() {
    this.teacherService.getTeachers().subscribe({
      next: (result) => {
        this.teacherList = result;
      },
      error: (err) => {
        console.log(err);
      },
      complete: () => {},
    });
  }

  private getCourseTeachers(courseId: string) {
    this.courseService.getCourseTeachers(courseId).subscribe({
      next: (result) => {
        this.teacherList = result;
      },
      error: (err) => {
        console.log(err);
      },
      complete: () => {},
    });
  }
}
