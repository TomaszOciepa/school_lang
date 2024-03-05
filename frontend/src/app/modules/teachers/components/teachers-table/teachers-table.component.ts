import {
  AfterViewInit,
  Component,
  ErrorHandler,
  ViewChild,
} from '@angular/core';
import { Router } from '@angular/router';
import { TeacherService } from 'src/app/modules/core/services/teacher.service';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { KeycloakService } from 'keycloak-angular';
import { User } from 'src/app/modules/core/models/user.model';

@Component({
  selector: 'app-teachers-table',
  templateUrl: './teachers-table.component.html',
  styleUrls: ['./teachers-table.component.css'],
})
export class TeachersTableComponent implements AfterViewInit {
  displayedColumns: string[] = [
    'lp',
    'firstName',
    'lastName',
    'email',
    'buttons',
  ];
  dataSource!: MatTableDataSource<User>;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(private teacherService: TeacherService) {}

  async ngAfterViewInit(): Promise<void> {
    this.teacherService.getTeachers().subscribe({
      next: (clients) => {
        console.log(clients);
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
