import { Component, ErrorHandler, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { User } from 'src/app/modules/core/models/user.model';
import { TeacherService } from 'src/app/modules/core/services/teacher.service';

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

  constructor(private teacherService: TeacherService) {}

  async ngOnInit(): Promise<void> {
    this.getTeachers();
  }

  private getTeachers() {
    this.teacherService.getTeachers('INACTIVE').subscribe({
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
