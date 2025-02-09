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

@Component({
  selector: 'app-student-lessons-table',
  templateUrl: './student-lessons-table.component.html',
  styleUrls: ['./student-lessons-table.component.css'],
})
export class StudentLessonsTableComponent {
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
  errMsg!: string;

  constructor(private lessonsService: LessonsService, private router: Router) {}

  async ngOnInit(): Promise<void> {
    this.getLessonsByTeacherId();
  }

  private getLessonsByTeacherId() {
    this.lessonsService.getLessonsByStudentId(this.studentId).subscribe({
      next: (lesson) => {
        // const sortedLessons = lesson.sort(
        //   (a, b) =>
        //     new Date(b.startDate).getTime() - new Date(a.startDate).getTime()
        // );
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
      (item) => item.studentId === this.studentId
    );
    return attendance ? attendance.present : false;
  }
}
