import { Component, ErrorHandler, Input, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Lesson } from 'src/app/modules/core/models/lesson.model';
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
  userId!: number;
  constructor(
    private lessonsService: LessonsService,
    private studentAccount: StudentAccountService
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
}
