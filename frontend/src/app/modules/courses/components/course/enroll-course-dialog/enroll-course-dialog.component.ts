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
import { error } from 'console';
import { response } from 'express';
import { User } from 'src/app/modules/core/models/user.model';
import { CourseService } from 'src/app/modules/core/services/course.service';
import { StudentService } from 'src/app/modules/core/services/student.service';
import { TeacherService } from 'src/app/modules/core/services/teacher.service';

@Component({
  selector: 'app-enroll-course-dialog',
  templateUrl: './enroll-course-dialog.component.html',
  styleUrls: ['./enroll-course-dialog.component.css'],
})
export class EnrollCourseDialogComponent implements OnInit {
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
  courseId!: string;
  usersIdEnrolled!: number[];
  students!: User[];
  enrollStudentIsEnable!: boolean;

  constructor(
    private dialogRef: MatDialogRef<EnrollCourseDialogComponent>,
    @Inject(MAT_DIALOG_DATA)
    private data: {
      enrollStudentIsEnable: boolean;
      courseId: string;
      listUserId: number[];
    },
    private courseService: CourseService,
    private studentServices: StudentService,
    private teacherService: TeacherService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.enrollStudentIsEnable = this.data.enrollStudentIsEnable;
    this.courseId = this.data.courseId;
    this.usersIdEnrolled = this.data.listUserId;
  }

  async ngAfterViewInit(): Promise<void> {
    console.log('siema: enableD:' + this.enrollStudentIsEnable);

    if (this.enrollStudentIsEnable) {
      if (this.usersIdEnrolled == undefined) {
        this.getStudents();
      } else {
        this.getStudentsByNotIdNumber();
      }
    }

    if (!this.enrollStudentIsEnable) {
      if (this.usersIdEnrolled == undefined) {
        this.getTeachers();
      } else {
        this.getTeachersByNotIdNumber();
      }
    }
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  enrollCourse(userId: number) {
    if (this.enrollStudentIsEnable) {
      this.enrollStudent(userId);
    }

    if (!this.enrollStudentIsEnable) {
      this.enrollTeacher(userId);
    }
  }

  private enrollStudent(studentId: number) {
    console.log('zapisz: ' + studentId);
    this.courseService
      .studentCourseEnrollment(this.courseId, studentId)
      .subscribe({
        next: (response) => {
          console.log('next: ' + response);
        },
        error: (err: HttpErrorResponse) => {
          console.log('error: ' + err.error.message);
        },
        complete: () => {
          console.log('zakończone');
          window.location.reload();
        },
      });
  }

  private enrollTeacher(teacherId: number) {
    console.log('zapisz: ' + teacherId);
    this.courseService
      .teacherCourseEnrollment(this.courseId, teacherId)
      .subscribe({
        next: (response) => {
          console.log('next: ' + response);
        },
        error: (err) => {
          console.log('error: ' + err);
        },
        complete: () => {
          console.log('zakończone');
          window.location.reload();
        },
      });
  }

  getStudentsByNotIdNumber() {
    this.studentServices
      .getStudentsByNotIdNumber(this.usersIdEnrolled)
      .subscribe({
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

  getStudents() {
    this.studentServices.getStudent().subscribe({
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

  getTeachers() {
    this.teacherService.getTeacher().subscribe({
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

  getTeachersByNotIdNumber() {
    this.teacherService
      .getTeachersByNotIdNumber(this.usersIdEnrolled)
      .subscribe({
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
}
