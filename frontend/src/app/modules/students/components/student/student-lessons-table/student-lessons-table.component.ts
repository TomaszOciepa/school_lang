import { Component, ErrorHandler, Input, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Lesson } from 'src/app/modules/core/models/lesson.model';
import { LessonsService } from 'src/app/modules/core/services/lessons.service';

@Component({
  selector: 'app-student-lessons-table',
  templateUrl: './student-lessons-table.component.html',
  styleUrls: ['./student-lessons-table.component.css'],
})
export class StudentLessonsTableComponent {
  displayedColumns: string[] = [
    'lp',
    'eventName',
    'startDate',
    'endDate',
    'status',
    'participantsNumber',
    'buttons',
  ];
  dataSource!: MatTableDataSource<Lesson>;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;
  @Input('student-id') studentId!: number;

  lessonsNumber!: number;
  errMsg!: string;

  constructor(private lessonsService: LessonsService) {}

  async ngOnInit(): Promise<void> {
    this.getLessonsByTeacherId();
  }

  private getLessonsByTeacherId() {
    this.lessonsService.getLessonsByStudentId(this.studentId).subscribe({
      next: (lesson) => {
        this.lessonsNumber = lesson.length;
        this.dataSource = new MatTableDataSource<Lesson>(lesson);
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
