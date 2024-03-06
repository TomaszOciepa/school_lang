import { HttpErrorResponse } from '@angular/common/http';
import {
  Component,
  ErrorHandler,
  Inject,
  OnInit,
  ViewChild,
} from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Router } from '@angular/router';
import { User } from 'src/app/modules/core/models/user.model';
import { LessonsService } from 'src/app/modules/core/services/lessons.service';
import { StudentService } from 'src/app/modules/core/services/student.service';

@Component({
  selector: 'app-enroll-lesson-dialog',
  templateUrl: './enroll-lesson-dialog.component.html',
  styleUrls: ['./enroll-lesson-dialog.component.css'],
})
export class EnrollLessonDialogComponent implements OnInit {
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

  usersIdEnrolled!: number[];
  students!: User[];
  lessonId!: string;

  constructor(
    private dialogRef: MatDialogRef<EnrollLessonDialogComponent>,
    @Inject(MAT_DIALOG_DATA)
    private data: {
      lessonId: string;
      listUserId: number[];
    },

    private studentServices: StudentService,
    private lessonService: LessonsService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.lessonId = this.data.lessonId;
    this.usersIdEnrolled = this.data.listUserId;
  }

  async ngAfterViewInit(): Promise<void> {
    console.log('co to jest: ' + this.usersIdEnrolled);
    if (this.usersIdEnrolled.length == 0) {
      console.log('dupa');
      this.getStudents();
    } else {
      console.log('blada');
      this.getStudentsByIdNumberNotEqual();
    }
  }

  private getStudents() {
    this.studentServices.getStudents().subscribe({
      next: (students) => {
        this.students = students;
        this.dataSource = new MatTableDataSource<User>(students);
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
      },
      error: (err: HttpErrorResponse) => {
        console.log(err);
      },
      complete: () => {},
    });
  }

  getStudentsByIdNumberNotEqual() {
    this.studentServices
      .getStudentsByIdNumberNotEqual(this.usersIdEnrolled)
      .subscribe({
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

  enrollStudentLesson(userId: number) {
    this.lessonService.enrollStudentLesson(this.lessonId, userId).subscribe({
      next: () => {},
      error: (err: HttpErrorResponse) => {
        console.log(err);
      },
      complete: () => {
        this.closeDialog();
        window.location.reload();
      },
    });
  }

  closeDialog() {
    this.dialogRef.close();
  }
}
