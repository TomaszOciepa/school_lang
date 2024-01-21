import { Component, ErrorHandler, Inject, ViewChild } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Router } from '@angular/router';
import { User } from 'src/app/modules/core/models/user.model';
import { CourseService } from 'src/app/modules/core/services/course.service';
import { StudentService } from 'src/app/modules/core/services/student.service';

@Component({
  selector: 'app-enroll-course-dialog',
  templateUrl: './enroll-course-dialog.component.html',
  styleUrls: ['./enroll-course-dialog.component.css'],
})
export class EnrollCourseDialogComponent {
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
  studentsIdEnrolled!: number[];
  students!: User[];

  constructor(
    private dialogRef: MatDialogRef<EnrollCourseDialogComponent>,
    @Inject(MAT_DIALOG_DATA)
    private data: { courseId: string; listStudentsId: number[] },
    private courseService: CourseService,
    private studentServices: StudentService,
    private router: Router
  ) {}

  ngOnInit() {
    this.courseId = this.data.courseId;
    this.studentsIdEnrolled = this.data.listStudentsId;
    console.log(this.studentsIdEnrolled);
  }

  async ngAfterViewInit(): Promise<void> {
    if (this.studentsIdEnrolled == undefined) {
      this.getStudents();
    } else this.getStudentsByNotIdNumber();
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }

  enrollCourse(studentId: number) {
    console.log('zapisz: ' + studentId);
    this.courseService
      .studentCourseEnrollment(this.courseId, studentId)
      .subscribe((response) => {
        console.log(response);
      });
    this.dialogRef.close();
    window.location.reload();
  }

  getStudentsByNotIdNumber() {
    this.studentServices
      .getStudentsByNotIdNumber(this.studentsIdEnrolled)
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
}
