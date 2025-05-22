import { Component, ErrorHandler, Input, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Router } from '@angular/router';
import { Course } from 'src/app/modules/core/models/course.model';
import { CourseService } from 'src/app/modules/core/services/course.service';

@Component({
  selector: 'app-courses-table',
  templateUrl: './courses-table.component.html',
  styleUrls: ['./courses-table.component.css'],
})
export class CoursesTableComponent {
  displayedColumns: string[] = [
    'lp',
    'name',
    'language',
    'startDate',
    'status',
    'price',
  ];
  dataSource!: MatTableDataSource<Course>;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  @Input('user-id') userId!: number;

  constructor(private courseService: CourseService, private router: Router) {}

  async ngOnInit(): Promise<void> {
    this.getCourseByStudent(this.userId);
  }

  private getCourseByStudent(id: number) {
    this.courseService.getCourseByStudentId(id).subscribe({
      next: (course) => {
        const sortedCourses = course.sort(
          (a, b) =>
            new Date(b.startDate).getTime() - new Date(a.startDate).getTime()
        );

        this.dataSource = new MatTableDataSource<Course>(sortedCourses);
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

  navigateToCourse(courseId: string) {
    this.router.navigate(['/account-student/course/', courseId]);
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
}
