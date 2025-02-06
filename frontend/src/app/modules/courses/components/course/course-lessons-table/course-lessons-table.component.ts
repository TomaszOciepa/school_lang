import { Component, ErrorHandler, Input, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Router } from '@angular/router';
import { Lesson } from 'src/app/modules/core/models/lesson.model';
import { LessonsService } from 'src/app/modules/core/services/lessons.service';

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
    'participantsNumber',
  ];
  dataSource!: MatTableDataSource<Lesson>;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;
  @Input('course-id') courseId!: string;
  @Input('lessons-limit') lessonsLimit!: number;

  lessonsNumber!: number;
  errMsg!: string;

  constructor(private lessonsService: LessonsService, private router: Router) {}

  async ngOnInit(): Promise<void> {
    this.getLessonsByCourseId();
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

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  addLesson() {
    this.router.navigate(['/lessons/dodaj', { id: this.courseId }]);
  }

  navigateToLesson(lessonId: string) {
    this.router.navigate(['/lessons', lessonId]);
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

  private hideErrorMsg() {
    setTimeout(() => {
      this.errMsg = '';
    }, 3000);
  }
}
