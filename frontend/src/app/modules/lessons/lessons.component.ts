import { Component, ErrorHandler, OnInit } from '@angular/core';
import { Lesson, LessonResponse } from '../core/models/lesson.model';
import { LessonsService } from '../core/services/lessons.service';
import { LoadUserProfileService } from '../core/services/load-user-profile.service';
import { TeacherService } from '../core/services/teacher.service';

@Component({
  selector: 'app-lessons',
  templateUrl: './lessons.component.html',
  styleUrls: ['./lessons.component.css'],
})
export class LessonsComponent {
  role!: string;
  teacherId!: number;
  lessonsList!: Lesson[];

  constructor(
    private lessonsService: LessonsService,
    private userProfileService: LoadUserProfileService,
    private teacherService: TeacherService
  ) {}

  async ngOnInit(): Promise<void> {
    this.loadUserProfile();
  }

  async loadUserProfile(): Promise<void> {
    await this.userProfileService.loadUserProfile();

    if (this.userProfileService.isAdmin) {
      this.role = 'ADMIN';
      this.getLessons();
    }

    if (this.userProfileService.isTeacher) {
      this.role = 'TEACHER';

      this.teacherService
        .getTeacherByEmail(this.userProfileService.userProfile?.email)
        .subscribe({
          next: (result) => {
            this.teacherId = result.id;
          },
          error: (err: ErrorHandler) => {
            console.log(err);
          },
          complete: () => {
            this.getLessonsByTeacherId(this.teacherId);
          },
        });
    }
  }

  private getLessonsByTeacherId(id: number) {
    this.lessonsService.getLessonByTeacherId(id).subscribe({
      next: (lesson) => {
        this.lessonsList = lesson;
        console.log(
          'Wielkość w getLessonsByTeacherId: ',
          this.lessonsList.length
        );
      },
      error: (err: ErrorHandler) => {
        console.log(err);
      },
    });
  }

  private getLessons() {
    this.lessonsService.getAllLessons().subscribe({
      next: (lesson) => {
        this.lessonsList = lesson;
        console.log('Wielkość w getLessons: ', this.lessonsList.length);
      },
      error: (err: ErrorHandler) => {
        console.log(err);
      },
    });
  }
}
