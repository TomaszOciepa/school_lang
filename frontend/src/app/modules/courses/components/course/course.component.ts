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

  listStudentsId!: number[];

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
      error: (err) => {
        console.log(err);
      },
    });
  }

  getCourseTeachers(courseId: string) {
    this.courseService.getCourseTeachers(courseId).subscribe({
      next: (teacher) => {
        this.teachers = teacher;
      },
      error: (err) => {
        console.log(err);
      },
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

  openEnrollDialog() {
    if (this.students !== undefined) {
      this.listStudentsId = this.students.map((student) => student.id);
    }

    const dialogRef = this.dialog.open(EnrollCourseDialogComponent, {
      data: {
        courseId: this.course.id,
        listStudentsId: this.listStudentsId,
      },
      width: '600px',
      maxWidth: '600px',
    });
  }

  openUnEnrollDialog(student: User) {
    const dialogRef = this.dialog.open(UnenrollCourseDialogComponent, {
      data: {
        courseId: this.course.id,
        student: student,
      },
      width: '600px',
      maxWidth: '600px',
    });
  }
}
