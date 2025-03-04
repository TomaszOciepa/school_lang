import { Component, ViewChild } from '@angular/core';
import { LessonResponse, Salary } from '../../core/models/salary.model';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { SalaryService } from '../../core/services/salary.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-user-salary-details',
  templateUrl: './user-salary-details.component.html',
  styleUrls: ['./user-salary-details.component.css'],
})
export class UserSalaryDetailsComponent {
  id!: string;
  salary!: Salary;

  displayedColumns: string[] = ['lp', 'wartość', 'status', 'startDate'];
  dataSource!: MatTableDataSource<LessonResponse>;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(
    private salaryService: SalaryService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.route.params;
    this.route.params.subscribe((params) => {
      this.id = params['id'];
    });
    this.getSalaryById(this.id);
  }

  getSalaryById(id: string) {
    this.salaryService.getSalaryById(id).subscribe({
      next: (response) => {
        this.salary = response;
        this.dataSource = new MatTableDataSource<LessonResponse>(
          response.lessons
        );
      },
      error: (err) => {
        console.log(err);
      },
      complete: () => {},
    });
  }

  getMonthName(date: Date | string): string {
    if (typeof date === 'string') {
      date = new Date(date);
    }

    if (!(date instanceof Date) || isNaN(date.getTime())) {
      throw new Error('Invalid Date object');
    }

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

  getStatusName(status: string): string {
    switch (status) {
      case 'IN_PROGRESS':
        return 'Oczekiwana';
      case 'FINISHED':
        return 'Zaksięgowana';
      default:
        return 'Nieznany';
    }
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

  navigateToLesson(lessonId: string) {
    this.router.navigate(['/lessons', lessonId]);
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

  getStatusColor(lesson: any): string {
    switch (lesson.status) {
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

  getLessonStatus(lesson: any): string {
    switch (lesson.status) {
      case 'ACTIVE':
        return 'W trakcie';
      case 'INACTIVE':
        return 'Oczekiwana';
      case 'FINISHED':
        return 'Zakończona';
      default:
        return 'Nieznany';
    }
  }
}
