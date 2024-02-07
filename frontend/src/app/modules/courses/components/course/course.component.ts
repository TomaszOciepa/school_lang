import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Course } from 'src/app/modules/core/models/course.model';
import { CourseService } from 'src/app/modules/core/services/course.service';
import { EditCourseDialogComponent } from './edit-course-dialog/edit-course-dialog.component';
import { MatDialog } from '@angular/material/dialog';
import { DeleteCourseDialogComponent } from './delete-course-dialog/delete-course-dialog.component';
import { User } from 'src/app/modules/core/models/user.model';
import { EnrollCourseDialogComponent } from './enroll-course-dialog/enroll-course-dialog.component';
import { UnenrollCourseDialogComponent } from './unenroll-course-dialog/unenroll-course-dialog.component';

@Component({
  selector: 'app-course',
  templateUrl: './course.component.html',
  styleUrls: ['./course.component.css'],
})
export class CourseComponent {
  id!: string;
  course!: Course;
  students!: User[];
  teachers!: User[];
  courseId!: string;

  listUserId!: number[];
  errMsg!: string;

  constructor(
    private courseService: CourseService,
    private route: ActivatedRoute,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.route.params;
    this.route.params.subscribe((params) => {
      this.id = params['id'];
    });

    this.getCourse(this.id);
    this.getCourseMembers(this.id);
    this.getCourseTeachers(this.id);
  }

  getCourse(id: string) {
    this.courseService.getCourseById(id).subscribe((response) => {
      this.course = response;
      this.courseId = response.id;
    });
  }

  getCourseMembers(courseId: string) {
    this.courseService.getCourseMembers(courseId).subscribe({
      next: (student) => {
        this.students = student;
      },
      error: (err) => {},
    });
  }

  getCourseTeachers(courseId: string) {
    this.courseService.getCourseTeachers(courseId).subscribe({
      next: (teacher) => {
        this.teachers = teacher;
      },
      error: (err) => {},
    });
  }

  openDialog() {
    const dialogRef = this.dialog.open(DeleteCourseDialogComponent, {
      data: {
        course: this.course,
      },
    });
  }

  openEditDialog() {
    const dialogRef = this.dialog.open(EditCourseDialogComponent, {
      data: {
        course: this.course,
      },
      width: '600px',
      maxWidth: '600px',
    });
  }

  openEnrollDialog(enrollStudentIsEnable: boolean) {
    this.selectStudentOrTeacherEnroll(enrollStudentIsEnable);

    if (this.course.status !== 'FULL' || !enrollStudentIsEnable) {
      const dialogRef = this.dialog.open(EnrollCourseDialogComponent, {
        data: {
          enrollStudentIsEnable: enrollStudentIsEnable,
          courseId: this.course.id,
          listUserId: this.listUserId,
        },
        width: '600px',
        maxWidth: '600px',
      });
    } else {
      this.errMsg = 'Course members is FULL';
      this.hideErrorMsg();
    }
  }

  private hideErrorMsg() {
    setTimeout(() => {
      this.errMsg = '';
    }, 3000);
  }

  private selectStudentOrTeacherEnroll(enrollStudent: boolean) {
    if (enrollStudent) {
      if (this.students !== undefined) {
        this.listUserId = this.students.map((student) => student.id);
      }
    }

    if (!enrollStudent) {
      if (this.teachers !== undefined) {
        this.listUserId = this.teachers.map((teacher) => teacher.id);
      }
    }
  }

  openUnEnrollDialog(user: User, studentIsEnabled: boolean) {
    const dialogRef = this.dialog.open(UnenrollCourseDialogComponent, {
      data: {
        studentIsEnabled: studentIsEnabled,
        courseId: this.course.id,
        user: user,
      },
      width: '600px',
      maxWidth: '600px',
    });
  }
}
