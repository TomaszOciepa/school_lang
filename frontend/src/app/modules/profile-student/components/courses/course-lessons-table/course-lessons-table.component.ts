import { Component, ErrorHandler, Input, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Router } from '@angular/router';
import {
  AttendanceList,
  Lesson,
} from 'src/app/modules/core/models/lesson.model';
import { LessonsService } from 'src/app/modules/core/services/lessons.service';
import { StudentAccountService } from 'src/app/modules/core/services/student-account.service';

@Component({
  selector: 'app-course-lessons-table',
  templateUrl: './course-lessons-table.component.html',
  styleUrls: ['./course-lessons-table.component.css'],
})
export class CourseLessonsTableComponent {
  displayedColumns: string[] = [
    'lp',
    'startDate',
    'hours',
    'status',
    'eventName',
    'present',
  ];
  dataSource!: MatTableDataSource<Lesson>;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  @Input('course-id') courseId!: string;

  userId!: number;
  lessonsList: Lesson[] = [];
  averageAttendance!: number;

  constructor(
    private lessonsService: LessonsService,
    private studentAccount: StudentAccountService,
    private router: Router
  ) {}

  async ngOnInit(): Promise<void> {
    this.loadUserProfile();
    this.getLessonsByCourseId(this.courseId);
  }

  async loadUserProfile(): Promise<void> {
    await this.studentAccount.loadUserProfile();
    this.userId = this.studentAccount.userId;
  }

  private getLessonsByCourseId(id: string) {
    this.lessonsService.getLessonsByCourseId(id).subscribe({
      next: (lesson) => {
        this.lessonsList = lesson;
        this.dataSource = new MatTableDataSource<Lesson>(lesson);
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
      },
      error: (err: ErrorHandler) => {
        console.log(err);
      },
      complete: () => {
        this.averageAttendance = this.getAverageAttendance(
          this.lessonsList,
          this.userId
        );
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
    this.router.navigate([
      `/account-student/lesson/${lessonId}/s/${this.userId}`,
    ]);
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

  getStatusAttendance(present: boolean): string {
    switch (present) {
      case true:
        return 'green';
      case false:
        return 'red';
      default:
        return 'red';
    }
  }

  getAttendance(attendanceList: AttendanceList[]): boolean {
    const attendance = attendanceList.find(
      (item) => item.studentId === this.userId
    );
    return attendance ? attendance.present : false;
  }

  getAverageAttendance(lessons: Lesson[], studentId: number): number {
    // Filtrowanie lekcji, które mają status FINISHED
    const finishedLessons = lessons.filter(
      (lesson) => lesson.status === 'FINISHED'
    );

    // Pobranie obecności studenta na tych lekcjach
    const studentAttendances = finishedLessons
      .map((lesson) =>
        lesson.attendanceList.find(
          (attendance) => attendance.studentId === studentId
        )
      )
      .filter((attendance) => attendance !== undefined) as AttendanceList[];

    // Jeśli student nie miał żadnych lekcji FINISHED, zwracamy 0
    if (studentAttendances.length === 0) {
      return 0;
    }

    // Obliczenie średniej obecności (liczba obecności / liczba lekcji FINISHED)
    const presentCount = studentAttendances.filter(
      (attendance) => attendance.present
    ).length;
    return (presentCount / studentAttendances.length) * 100; // Zwracamy procent
  }
}
