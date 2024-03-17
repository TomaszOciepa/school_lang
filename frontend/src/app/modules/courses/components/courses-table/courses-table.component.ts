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
import { Course } from 'src/app/modules/core/models/course.model';
import { CourseService } from 'src/app/modules/core/services/course.service';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-courses-table',
  templateUrl: './courses-table.component.html',
  styleUrls: ['./courses-table.component.css'],
  providers: [DatePipe],
})
export class CoursesTableComponent {
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
  @Input('switch') switch!: string;
  @Input('teacherId') teacherId!: number;
  @Input('studentId') studentId!: number;

  constructor(private courseService: CourseService) {}

  async ngOnInit(): Promise<void> {
    if (this.switch === 'teacher') {
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
    } else if (this.switch === 'student') {
      console.log();
      this.courseService.getCourseByStudentId(this.studentId).subscribe({
        next: (course) => {
          this.dataSource = new MatTableDataSource<Course>(course);
          this.dataSource.paginator = this.paginator;
          this.dataSource.sort = this.sort;
        },
        error: (err: ErrorHandler) => {
          console.log(err);
        },
      });
    } else {
      this.courseService.getAllByStatus().subscribe({
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
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }
}
