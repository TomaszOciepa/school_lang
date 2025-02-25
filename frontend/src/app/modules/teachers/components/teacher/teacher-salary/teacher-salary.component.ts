import { Component, ErrorHandler, Input, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Salary } from 'src/app/modules/core/models/salary.model';
import { SalaryService } from 'src/app/modules/core/services/salary.service';

@Component({
  selector: 'app-teacher-salary',
  templateUrl: './teacher-salary.component.html',
  styleUrls: ['./teacher-salary.component.css'],
})
export class TeacherSalaryComponent {
  displayedColumns: string[] = ['lp', 'date', 'status', 'kwota'];

  dataSource!: MatTableDataSource<Salary>;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  @Input('teacher-id') teacherId!: number;

  constructor(private salaryService: SalaryService) {}

  async ngOnInit(): Promise<void> {
    this.salaryService.getSalary(this.teacherId).subscribe({
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
}
