import {
  AfterViewInit,
  Component,
  ErrorHandler,
  ViewChild,
} from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { User } from 'src/app/modules/core/models/user.model';
import { LoadUserProfileService } from 'src/app/modules/core/services/load-user-profile.service';
import { StudentService } from 'src/app/modules/core/services/student.service';

@Component({
  selector: 'app-students-table',
  templateUrl: './students-table.component.html',
  styleUrls: ['./students-table.component.css'],
})
export class StudentsTableComponent {
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

  status!: string;
  role!: string;

  constructor(
    private userProfileService: LoadUserProfileService,
    private studentService: StudentService
  ) {}

  async ngOnInit(): Promise<void> {
    this.loadUserProfile();
  }

  private getStudents() {
    this.studentService.getStudents(this.status).subscribe({
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

    this.getStudents();
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }
}
