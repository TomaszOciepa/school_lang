import { Component, ErrorHandler, ViewChild } from '@angular/core';
import { TeacherService } from 'src/app/modules/core/services/teacher.service';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { User } from 'src/app/modules/core/models/user.model';
import { KeycloakService } from 'keycloak-angular';
import { LoadUserProfileService } from 'src/app/modules/core/services/load-user-profile.service';
import { KeycloakProfile } from 'keycloak-js';

@Component({
  selector: 'app-teachers-table',
  templateUrl: './teachers-table.component.html',
  styleUrls: ['./teachers-table.component.css'],
})
export class TeachersTableComponent {
  displayedColumns: string[] = [
    'lp',
    'firstName',
    'lastName',
    'email',
    'status',
    'buttons',
  ];
  dataSource!: MatTableDataSource<User>;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  status!: string;
  role!: string;

  constructor(
    private teacherService: TeacherService,
    private userProfileService: LoadUserProfileService
  ) {}

  async ngOnInit(): Promise<void> {
    this.loadUserProfile();
  }

  async loadUserProfile(): Promise<void> {
    await this.userProfileService.loadUserProfile();

    if (this.userProfileService.isAdmin) {
      this.status = '';
      this.role = 'ADMIN';
    }

    if (this.userProfileService.isTeacher) {
      this.status = 'ACTIVE';
      this.role = 'TEACHER';
    }

    this.getTeachers();
  }

  private getTeachers() {
    this.teacherService.getTeachers(this.status).subscribe({
      next: (clients) => {
        this.dataSource = new MatTableDataSource<User>(clients);
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
