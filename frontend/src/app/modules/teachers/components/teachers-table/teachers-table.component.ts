import { Component, ErrorHandler, ViewChild } from '@angular/core';
import { TeacherService } from 'src/app/modules/core/services/teacher.service';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { User } from 'src/app/modules/core/models/user.model';
import { KeycloakService } from 'keycloak-angular';
import { LoadUserProfileService } from 'src/app/modules/core/services/load-user-profile.service';
import { KeycloakProfile } from 'keycloak-js';
import { Router } from '@angular/router';

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
  ];
  dataSource!: MatTableDataSource<User>;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  status!: string;
  role!: string;

  constructor(
    private readonly keycloak: KeycloakService,
    private teacherService: TeacherService,
    private userProfileService: LoadUserProfileService,
    private router: Router
  ) {}

  async ngOnInit(): Promise<void> {
    this.loadUserProfile();
    this.getTeachers();
  }

  async loadUserProfile(): Promise<void> {
    await this.userProfileService.loadUserProfile();

    if (!this.userProfileService.isLoggedIn) {
      this.login();
    }

    if (this.userProfileService.isAdmin) {
      this.role = 'ADMIN';
    }
  }

  private getTeachers() {
    this.teacherService.getTeachers().subscribe({
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

  login() {
    this.keycloak.login();
  }

  navigateToTeacher(teacherId: number) {
    this.router.navigate(['/teachers', teacherId]);
  }
}
