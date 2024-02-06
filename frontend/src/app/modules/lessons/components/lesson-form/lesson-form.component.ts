import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Observer } from 'rxjs';
import { Course } from 'src/app/modules/core/models/course.model';
import {
  Lesson,
  PostLessonForm,
} from 'src/app/modules/core/models/lesson.model';
import { User } from 'src/app/modules/core/models/user.model';
import { CourseService } from 'src/app/modules/core/services/course.service';
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

  coursesList!: Course[];
  teacherList!: User[];

  observer: Observer<unknown> = {
    next: () => {
      if (this.editMode) {
        this.emitCLoseDialog();
      }
      console.log('Zapisano do bazy:');
    },
    error: (err) => {
      console.log(err);
    },
    complete: () => {
      this.router.navigate(['/lessons']);
    },
  };

  @Output() closeDialog = new EventEmitter<void>();

  constructor(
    private formService: FormsService,
    private lessonsService: LessonsService,
    private courseService: CourseService,
    private teacherService: TeacherService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.getTeachers();
    this.getCourse();
    this.initForm();
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
        this.editMode ? this.lesson.startDate : new Date(),
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      startTime: new FormControl(
        // this.editMode ? this.lesson.startDate.getTime.toString() :
        '',
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      endTime: new FormControl(
        // this.editMode ? this.lesson.endDate.getTime.toString() :
        '',
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      courseId: new FormControl(this.editMode ? this.lesson.courseId : '', {
        nonNullable: true,
        validators: [],
      }),
      teacherId: new FormControl(
        this.editMode ? this.lesson.teacherId.toString() : '',
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      status: new FormControl(this.editMode ? this.lesson.status : '', {
        nonNullable: true,
        validators: [Validators.required],
      }),
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
    console.log(this.lessonForm.getRawValue());

    this.lesson.eventName = this.lessonForm.getRawValue().eventName;
    const startDate = this.lessonForm.getRawValue().startDate;

    this.lesson.startDate = this.generateDateTime(
      startDate,
      this.lessonForm.getRawValue().startTime
    );

    this.lesson.endDate = this.generateDateTime(
      startDate,
      this.lessonForm.getRawValue().endTime
    );

    this.lesson.courseId = this.lessonForm.getRawValue().courseId;

    this.lesson.teacherId = parseInt(
      this.lessonForm.getRawValue().teacherId,
      10
    );
    this.lesson.status = this.lessonForm.getRawValue().status;
    this.lesson.description = this.lessonForm.getRawValue().description;

    if (this.editMode) {
      this.lessonsService
        .patchLesson(this.lesson.id, this.lesson)
        .subscribe(this.observer);
      return;
    }
    this.lessonsService.addLesson(this.lesson).subscribe(this.observer);
  }

  emitCLoseDialog() {
    this.closeDialog.emit();
  }

  private generateDateTime(date: Date, startTime: string): Date {
    const parsedTime = this.parseTimeString(startTime);
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

  private parseTimeString(
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

  private getCourse() {
    this.courseService.getCourses().subscribe({
      next: (result) => {
        this.coursesList = result;
      },
      error: (err) => {
        console.log(err);
      },
      complete: () => {},
    });
  }

  private getTeachers() {
    this.teacherService.getTeacher().subscribe({
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
