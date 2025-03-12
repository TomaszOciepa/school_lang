import { Component, ErrorHandler, Input, ViewChild } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { Lesson } from '../../core/models/lesson.model';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { LessonsService } from '../../core/services/lessons.service';

@Component({
  selector: 'app-lessons-table',
  templateUrl: './lessons-table.component.html',
  styleUrls: ['./lessons-table.component.css'],
})
export class LessonsTableComponent {
  displayedColumns: string[] = ['lp', 'startDate', 'endDate'];
  dataSource!: MatTableDataSource<Lesson>;
  @ViewChild(MatSort) sort!: MatSort;
  @Input('course-id') courseId!: string;

  constructor(private lessonsService: LessonsService) {}

  async ngOnInit(): Promise<void> {
    this.getLessonsByCourseId();
  }

  private getLessonsByCourseId() {
    this.lessonsService.getLessonsByCourseId(this.courseId).subscribe({
      next: (lesson) => {
        this.dataSource = new MatTableDataSource<Lesson>(lesson);
        this.dataSource.sort = this.sort;
      },
      error: (err: ErrorHandler) => {
        console.log(err);
      },
      complete: () => {},
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
