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
import { StudentService } from 'src/app/modules/core/services/student.service';

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
    'language',
    'startDate',
    'status',
    'price',
    'participantsNumber',
    'buttons',
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
    private teacherService: TeacherService
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
        this.dataSource = new MatTableDataSource<Course>(course);
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

  login() {
    this.keycloak.login();
  }
}
