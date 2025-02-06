import { Component, ErrorHandler, Input, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Router } from '@angular/router';
import { Course } from 'src/app/modules/core/models/course.model';
import { CourseService } from 'src/app/modules/core/services/course.service';
import { LoadUserProfileService } from 'src/app/modules/core/services/load-user-profile.service';

@Component({
  selector: 'app-teacher-courses-table',
  templateUrl: './teacher-courses-table.component.html',
  styleUrls: ['./teacher-courses-table.component.css'],
})
export class TeacherCoursesTableComponent {
  displayedColumns: string[] = [
    'lp',
    'startDate',
    'language',
    'status',
    'name',
  ];
  dataSource!: MatTableDataSource<Course>;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;
  @Input('teacherId') teacherId!: number;

  role!: string;

  constructor(
    private userProfileService: LoadUserProfileService,
    private courseService: CourseService,
    private router: Router
  ) {}

  async ngOnInit(): Promise<void> {
    this.loadUserProfile();
    this.courseService.getCourseByTeacherId(this.teacherId).subscribe({
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

  async loadUserProfile(): Promise<void> {
    await this.userProfileService.loadUserProfile();
    if (this.userProfileService.isAdmin) {
      this.role = 'ADMIN';
    }
  }

  navigateToCourse(courseId: string) {
    this.router.navigate(['/courses', courseId]);
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
