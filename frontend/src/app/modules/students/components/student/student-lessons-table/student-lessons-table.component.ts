import {
  Component,
  ErrorHandler,
  Input,
  ViewChild,
  OnChanges,
  SimpleChanges,
} from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Router } from '@angular/router';
import {
  AttendanceList,
  Lesson,
} from 'src/app/modules/core/models/lesson.model';
import { LessonsService } from 'src/app/modules/core/services/lessons.service';

@Component({
  selector: 'app-student-lessons-table',
  templateUrl: './student-lessons-table.component.html',
  styleUrls: ['./student-lessons-table.component.css'],
})
export class StudentLessonsTableComponent implements OnChanges {
  @Input('select-course-id') selectCourseId!: string;

  ngOnChanges(changes: SimpleChanges) {
    if (changes['selectCourseId'] && changes['selectCourseId'].currentValue) {
      this.getLessonsById(changes['selectCourseId'].currentValue);
    }
  }

  displayedColumns: string[] = [
    'lp',
    'startDate',
    'hour',
    'language',
    'status',
    'present',
  ];

  dataSource!: MatTableDataSource<Lesson>;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;
  @Input('student-id') studentId!: number;

  lessonsNumber!: number;
  groupLessonsMap!: Map<string, Lesson[]>;
  errMsg!: string;
  averageAttendance!: number;

  constructor(private lessonsService: LessonsService, private router: Router) {}

  async ngOnInit(): Promise<void> {
    this.getLessonsByStudentId();
  }

  private getLessonsByStudentId() {
    this.lessonsService.getLessonsByStudentId(this.studentId).subscribe({
      next: (lesson) => {
        this.lessonsNumber = lesson.length;
        this.groupLessonsByCourse(lesson);
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

  navigateToLesson(lessonId: string) {
    this.router.navigate(['/lessons', lessonId]);
  }

  getLanguageName(language: string): string {
    switch (language) {
      case 'ENGLISH':
        return 'Angielski';
      case 'POLISH':
        return 'Polski';
      case 'GERMAN':
        return 'Niemiecki';
      default:
        return 'Nieznany';
    }
  }

  getStatusClass(status: string): string {
    switch (status) {
      case 'ACTIVE':
        return 'green';
      case 'INACTIVE':
        return 'orange';
      case 'FINISHED':
        return 'gray';
      default:
        return '';
    }
  }

  getStatusName(status: string): string {
    switch (status) {
      case 'ACTIVE':
        return 'W trakcie';
      case 'INACTIVE':
        return 'Oczekiwana';
      case 'FINISHED':
        return 'Zakończona';
      default:
        return '';
    }
  }

  getStatusAttendance(present: boolean): string {
    switch (present) {
      case true:
        return 'blue';
      case false:
        return 'red';
      default:
        return 'red';
    }
  }

  getStatusAttendanceText(present: boolean): string {
    switch (present) {
      case true:
        return 'obecny';
      case false:
        return 'nieobecny';
      default:
        return 'red';
    }
  }

  getAttendance(attendanceList: AttendanceList[]): boolean {
    const attendance = attendanceList.find(
      (item) => item.studentId === this.studentId
    );
    return attendance ? attendance.present : false;
  }

  getAverageAttendance(lessons: Lesson[], studentId: number): number {
    const finishedLessons = lessons.filter(
      (lesson) => lesson.status === 'FINISHED'
    );

    const studentAttendances = finishedLessons
      .map((lesson) =>
        lesson.attendanceList.find(
          (attendance) => attendance.studentId === studentId
        )
      )
      .filter((attendance) => attendance !== undefined) as AttendanceList[];

    if (studentAttendances.length === 0) {
      return 0;
    }

    const presentCount = studentAttendances.filter(
      (attendance) => attendance.present
    ).length;
    return (presentCount / studentAttendances.length) * 100;
  }

  getLessonsById(courseId: string) {
    const lessons: Lesson[] = this.groupLessonsMap.get(courseId) || [];
    this.dataSource = new MatTableDataSource<Lesson>(lessons);
    this.averageAttendance = this.getAverageAttendance(lessons, this.studentId);
  }

  groupLessonsByCourse(lessons: Lesson[]) {
    const lessonsMap = new Map<string, Lesson[]>();

    lessons.forEach((lesson) => {
      if (!lessonsMap.has(lesson.courseId)) {
        lessonsMap.set(lesson.courseId, []);
      }
      lessonsMap.get(lesson.courseId)?.push(lesson);
    });

    this.groupLessonsMap = lessonsMap;
  }
}
