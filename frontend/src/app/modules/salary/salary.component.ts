import { Component, ErrorHandler, ViewChild } from '@angular/core';
import { SalaryService } from '../core/services/salary.service';
import { Router } from '@angular/router';
import { LoadUserProfileService } from '../core/services/load-user-profile.service';
import { TeacherService } from '../core/services/teacher.service';
import { MatTableDataSource } from '@angular/material/table';
import { Salary } from '../core/models/salary.model';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';

@Component({
  selector: 'app-salary',
  templateUrl: './salary.component.html',
  styleUrls: ['./salary.component.css'],
})
export class SalaryComponent {
  displayedColumns: string[] = ['lp', 'date', 'status', 'kwota'];
  dataSource!: MatTableDataSource<Salary>;
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  // user: User = {} as User;
  teacherId!: number;

  constructor(
    private userProfileService: LoadUserProfileService,
    private teacherService: TeacherService,
    private salaryService: SalaryService,
    private router: Router
  ) {}

  async ngOnInit(): Promise<void> {
    this.loadUserProfile();
  }

  async loadUserProfile(): Promise<void> {
    await this.userProfileService.loadUserProfile();

    if (this.userProfileService.isLoggedIn) {
      this.getTeacher(this.userProfileService.userProfile?.email);
    }
  }

  getTeacher(email?: string) {
    this.teacherService.getTeacherByEmail(email).subscribe({
      next: (result) => {
        this.teacherId = result.id;
      },
      error: () => {},
      complete: () => {
        this.getSalary(this.teacherId);
      },
    });
  }

  getSalary(id: number) {
    this.salaryService.getSalary(id).subscribe({
      next: (salary) => {
        const sortedSalary = salary.sort(
          (a, b) => new Date(b.date).getTime() - new Date(a.date).getTime()
        );

        this.dataSource = new MatTableDataSource<Salary>(sortedSalary);
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
        console.log(salary);
      },
      error: (err: ErrorHandler) => {
        console.log(err);
      },
    });
  }

  getMonthName(dateString: string): string {
    const date = new Date(dateString);
    const monthNames = [
      'styczeń',
      'luty',
      'marzec',
      'kwiecień',
      'maj',
      'czerwiec',
      'lipiec',
      'sierpień',
      'wrzesień',
      'październik',
      'listopad',
      'grudzień',
    ];
    return monthNames[date.getMonth()];
  }

  getLessonStatus(lesson: any): string {
    switch (lesson.status) {
      case 'IN_PROGRESS':
        return 'Oczekiwana';
      case 'FINISHED':
        return 'Zaksięgowana';
      default:
        return 'Nieznany';
    }
  }
  getStatusColor(lesson: any): string {
    switch (lesson.status) {
      case 'IN_PROGRESS':
        return '#FFD700';
      case 'FINISHED':
        return '#4CAF50';
      default:
        return 'transparent';
    }
  }

  navigateToDetails(salary: Salary) {
    console.log(salary || JSON);
    this.router.navigate(['/salary/details', salary.id]);
  }
}
