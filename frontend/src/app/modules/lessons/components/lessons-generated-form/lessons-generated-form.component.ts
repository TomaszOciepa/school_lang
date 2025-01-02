import { HttpErrorResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Observer } from 'rxjs';
import {
  GenerateLessonsResponse,
  LessonsGeneratedForm,
} from 'src/app/modules/core/models/lesson.model';
import { User } from 'src/app/modules/core/models/user.model';
import { CourseService } from 'src/app/modules/core/services/course.service';
import { FormsService } from 'src/app/modules/core/services/forms.service';
import { LessonsService } from 'src/app/modules/core/services/lessons.service';
import { TeacherService } from 'src/app/modules/core/services/teacher.service';

@Component({
  selector: 'app-lessons-generated-form',
  templateUrl: './lessons-generated-form.component.html',
  styleUrls: ['./lessons-generated-form.component.css'],
})
export class LessonsGeneratedFormComponent {
  lessonsGeneratedForm!: FormGroup<LessonsGeneratedForm>;
  courseId!: string | null;
  teacherList: User[] = [];
  errMsg!: string;
  generateLesson: GenerateLessonsResponse = {} as GenerateLessonsResponse;

  observer: Observer<unknown> = {
    next: () => {},
    error: (err: HttpErrorResponse) => {
      this.errMsg = err.error.message;
    },
    complete: () => {
      if (this.courseId !== null) {
        this.router.navigate(['/courses/' + this.courseId]);
      }
    },
  };

  constructor(
    private formService: FormsService,
    private lessonsService: LessonsService,
    private courseService: CourseService,
    private teacherService: TeacherService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.courseId = this.route.snapshot.paramMap.get('id');

    if (this.courseId !== null) {
      this.getCourseTeachers(this.courseId);
    }

    this.initForm();
  }

  get controls() {
    return this.lessonsGeneratedForm.controls;
  }

  private initForm() {
    this.lessonsGeneratedForm = new FormGroup<LessonsGeneratedForm>({
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

  getErrorMessage(control: FormControl) {
    return this.formService.getErrorMessage(control);
  }

  generateLessons() {
    if (this.lessonsGeneratedForm.get('timeRange')?.dirty) {
      this.generateLesson.timeRange =
        this.lessonsGeneratedForm.getRawValue().timeRange;
    }

    if (this.lessonsGeneratedForm.get('lessonDuration')?.dirty) {
      this.generateLesson.lessonDuration = parseInt(
        this.lessonsGeneratedForm.getRawValue().lessonDuration,
        10
      );
    }

    if (this.lessonsGeneratedForm.get('teacherId')?.dirty) {
      this.generateLesson.teacherId = parseInt(
        this.lessonsGeneratedForm.getRawValue().teacherId,
        10
      );
    }

    if (this.lessonsGeneratedForm.get('lessonFrequency')?.dirty) {
      this.generateLesson.lessonFrequency =
        this.lessonsGeneratedForm.getRawValue().lessonFrequency;
    }

    if (this.courseId != null) {
      this.generateLesson.courseId = this.courseId;
    }

    console.log(this.generateLesson);

    this.lessonsService
      .generateLessons(this.generateLesson)
      .subscribe(this.observer);
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
