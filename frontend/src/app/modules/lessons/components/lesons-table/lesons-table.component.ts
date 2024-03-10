import {
  AfterViewInit,
  Component,
  ErrorHandler,
  Input,
  ViewChild,
} from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Router } from '@angular/router';
import { Lesson } from 'src/app/modules/core/models/lesson.model';
import { LessonsService } from 'src/app/modules/core/services/lessons.service';

@Component({
  selector: 'app-lesons-table',
  templateUrl: './lesons-table.component.html',
  styleUrls: ['./lesons-table.component.css'],
})
export class LesonsTableComponent implements AfterViewInit {
  displayedColumns: string[] = [
    'lp',
    'eventName',
    'startDate',
    'endDate',
    'status',
    'participantsNumber',
    'buttons',
  ];
  dataSource!: MatTableDataSource<Lesson>;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;
  @Input('course-id') courseId!: string;
  @Input('teacher-id') teacherId!: number;
  @Input('student-id') studentId!: number;
  @Input('lessons-limit') lessonsLimit!: number;
  @Input('switch') switch: string = 'lesson';

  lessonsNumber!: number;
  errMsg!: string;

  constructor(private lessonsService: LessonsService, private router: Router) {}

  async ngAfterViewInit(): Promise<void> {
    if (this.switch === 'lesson') {
      this.getAllLessons();
    } else if (this.switch === 'course') {
      this.getLessonsByCourseId();
    } else if (this.switch === 'teacher') {
      this.getLessonsByTeacherId();
    } else if (this.switch === 'student') {
      this.getLessonsByStudentId();
    }
  }

  private getAllLessons() {
    this.lessonsService.getAllLessons().subscribe({
      next: (lesson) => {
        this.dataSource = new MatTableDataSource<Lesson>(lesson);
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
      },
      error: (err: ErrorHandler) => {
        console.log(err);
      },
    });
  }

  private getLessonsByCourseId() {
    this.lessonsService.getLessonsByCourseId(this.courseId).subscribe({
      next: (lesson) => {
        this.lessonsNumber = lesson.length;
        this.dataSource = new MatTableDataSource<Lesson>(lesson);
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
      },
      error: (err: ErrorHandler) => {
        console.log(err);
      },
    });
  }

  private getLessonsByTeacherId() {
    this.lessonsService.getLessonByTeacherId(this.teacherId).subscribe({
      next: (lesson) => {
        this.lessonsNumber = lesson.length;
        this.dataSource = new MatTableDataSource<Lesson>(lesson);
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
      },
      error: (err: ErrorHandler) => {
        console.log(err);
      },
    });
  }

  private getLessonsByStudentId() {
    this.lessonsService.getLessonsByStudentId(this.studentId).subscribe({
      next: (lesson) => {
        this.lessonsNumber = lesson.length;
        this.dataSource = new MatTableDataSource<Lesson>(lesson);
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
      },
      error: (err: ErrorHandler) => {
        console.log(err);
      },
    });
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  addCourseLesson() {
    if (this.lessonsLimit == this.lessonsNumber) {
      this.errMsg = 'Course Lesson Limit Reached.';
      this.hideErrorMsg();
    } else {
      this.router.navigate(['/lessons/dodaj', { id: this.courseId }]);
    }
  }

  addTeacherLesson() {
    if (this.lessonsLimit == this.lessonsNumber) {
      this.errMsg = 'Course Lesson Limit Reached.';
      this.hideErrorMsg();
    } else {
      this.router.navigate(['/lessons/dodaj', { teacherId: this.teacherId }]);
    }
  }

  addLesson() {
    this.router.navigate(['/lessons/dodaj']);
  }

  private hideErrorMsg() {
    setTimeout(() => {
      this.errMsg = '';
    }, 3000);
  }
}
