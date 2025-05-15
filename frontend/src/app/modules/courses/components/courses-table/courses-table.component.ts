import { Component, ErrorHandler, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Course } from 'src/app/modules/core/models/course.model';
import { CourseService } from 'src/app/modules/core/services/course.service';
import { DatePipe } from '@angular/common';
import { LoadUserProfileService } from 'src/app/modules/core/services/load-user-profile.service';
import { TeacherService } from 'src/app/modules/core/services/teacher.service';
import { KeycloakService } from 'keycloak-angular';
import { Router } from '@angular/router';

@Component({
  selector: 'app-courses-table',
  templateUrl: './courses-table.component.html',
  styleUrls: ['./courses-table.component.css'],
  providers: [DatePipe],
})
export class CoursesTableComponent {
  displayedColumns: string[] = [
    'lp',
    'startDate',
    'language',
    'status',
    'name',
    'price',
  ];
  dataSource!: MatTableDataSource<Course>;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  role!: string;
  userId!: number;

  constructor(
    private readonly keycloak: KeycloakService,
    private courseService: CourseService,
    private userProfileService: LoadUserProfileService,
    private teacherService: TeacherService,
    private router: Router
  ) {}

  async ngOnInit(): Promise<void> {
    this.loadUserProfile();
  }

  async loadUserProfile(): Promise<void> {
    await this.userProfileService.loadUserProfile();

    if (!this.userProfileService.isLoggedIn) {
      this.login();
    }

    if (this.userProfileService.isAdmin) {
      this.role = 'ADMIN';
      this.getCourses();
    }

    if (this.userProfileService.isTeacher) {
      this.role = 'TEACHER';

      this.teacherService
        .getTeacherByEmail(this.userProfileService.userProfile?.email)
        .subscribe({
          next: (result) => {
            this.userId = result.id;
          },
          error: (err: ErrorHandler) => {
            console.log(err);
          },
          complete: () => {
            this.getCourseByTeacher(this.userId);
          },
        });
    }
  }

  private getCourseByTeacher(id: number) {
    this.courseService.getCourseByTeacherId(id).subscribe({
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

  private getCourses() {
    this.courseService.getAllByStatus().subscribe({
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

  login() {
    this.keycloak.login();
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
