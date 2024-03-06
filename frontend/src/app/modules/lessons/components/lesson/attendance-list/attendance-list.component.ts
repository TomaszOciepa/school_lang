import { Component, Input, OnInit } from '@angular/core';
import {
  AttendanceList,
  Lesson,
} from 'src/app/modules/core/models/lesson.model';
import { StudentService } from 'src/app/modules/core/services/student.service';
import { User } from 'src/app/modules/core/models/user.model';
import { LessonsService } from 'src/app/modules/core/services/lessons.service';
import { UnEnrollLessonDialogComponent } from '../un-enroll-lesson-dialog/un-enroll-lesson-dialog.component';
import { MatDialog } from '@angular/material/dialog';

@Component({
  selector: 'app-attendance-list',
  templateUrl: './attendance-list.component.html',
  styleUrls: ['./attendance-list.component.css'],
})
export class AttendanceListComponent implements OnInit {
  @Input('lesson') lesson!: Lesson;
  @Input('attendance-list') attendanceList: AttendanceList[] = [];
  @Input('courseId') courseId!: string;

  idList: number[] = [];
  newLesson: Lesson = {} as Lesson;
  students!: User[];
  checked: boolean = false;

  constructor(
    private studentService: StudentService,
    private lessonsService: LessonsService,
    private dialog: MatDialog
  ) {}
  ngOnInit(): void {
    this.createIdList(this.attendanceList);
  }

  createIdList(attendanceList: AttendanceList[]) {
    if (attendanceList.length > 0) {
      for (let item of attendanceList) {
        this.idList.push(item.studentId);
      }
    }

    this.getStudents(this.idList);
  }

  getStudents(idList: number[]) {
    this.studentService.getStudentsByIdNumbers(idList).subscribe({
      next: (students) => {
        this.students = students;
        this.attendanceList.forEach((attendance) => {
          const student = students.find((s) => s.id === attendance.studentId);
          if (student) {
            attendance.firstName = student.firstName;
            attendance.lastName = student.lastName;
          }
        });
      },
      error: (err) => {
        console.log(err);
      },
    });
  }

  changeAttendance(id: number) {
    this.attendanceList[id].present = !this.attendanceList[id].present;
    this.checked = true;
  }

  save() {
    this.newLesson.attendanceList = this.lesson.attendanceList;
    this.lessonsService.patchLesson(this.lesson.id, this.newLesson).subscribe({
      next: (lesson) => {
        console.log(lesson);
      },
      error: (err) => {
        console.log(err);
      },
      complete: () => {
        this.checked = false;
      },
    });
  }

  openUnEnrollDialog(user: AttendanceList) {
    const dialogRef = this.dialog.open(UnEnrollLessonDialogComponent, {
      data: {
        lessonId: this.lesson.id,
        student: user,
      },
      width: '600px',
      maxWidth: '600px',
    });
  }
}
