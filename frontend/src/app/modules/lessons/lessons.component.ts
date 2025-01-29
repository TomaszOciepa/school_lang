import { Component, ErrorHandler, OnInit } from '@angular/core';
import { Lesson, LessonResponse } from '../core/models/lesson.model';
import { LessonsService } from '../core/services/lessons.service';
import { LoadUserProfileService } from '../core/services/load-user-profile.service';
import { TeacherService } from '../core/services/teacher.service';

export interface PeriodicElement {
  name: string;
  position: number;
  weight: number;
  symbol: string;
}

const ELEMENT_DATA: PeriodicElement[] = [
  { position: 1, name: 'Hydrogen', weight: 1.0079, symbol: 'H' },
  { position: 2, name: 'Helium', weight: 4.0026, symbol: 'He' },
  { position: 3, name: 'Lithium', weight: 6.941, symbol: 'Li' },
  { position: 4, name: 'Beryllium', weight: 9.0122, symbol: 'Be' },
  { position: 5, name: 'Boron', weight: 10.811, symbol: 'B' },
  { position: 6, name: 'Carbon', weight: 12.0107, symbol: 'C' },
  { position: 7, name: 'Nitrogen', weight: 14.0067, symbol: 'N' },
  { position: 8, name: 'Oxygen', weight: 15.9994, symbol: 'O' },
  { position: 9, name: 'Fluorine', weight: 18.9984, symbol: 'F' },
  { position: 10, name: 'Neon', weight: 20.1797, symbol: 'Ne' },
];

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

  displayedColumns: string[] = [
    'demo-position',
    'demo-name',
    'demo-weight',
    'demo-symbol',
  ];
  dataSource = ELEMENT_DATA;
}
