import { Component, ErrorHandler, Input, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Course } from 'src/app/modules/core/models/course.model';
import { CourseService } from 'src/app/modules/core/services/course.service';

@Component({
  selector: 'app-teacher-courses-table',
  templateUrl: './teacher-courses-table.component.html',
  styleUrls: ['./teacher-courses-table.component.css'],
})
export class TeacherCoursesTableComponent {
  displayedColumns: string[] = [
    'lp',
    'name',
    'startDate',
    'status',
    'participantsNumber',
    'buttons',
  ];
  dataSource!: MatTableDataSource<Course>;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;
  @Input('teacherId') teacherId!: number;

  constructor(private courseService: CourseService) {}

  async ngOnInit(): Promise<void> {
    this.courseService.getCourseByTeacherId(this.teacherId).subscribe({
      next: (course) => {
        this.dataSource = new MatTableDataSource<Course>(course);
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