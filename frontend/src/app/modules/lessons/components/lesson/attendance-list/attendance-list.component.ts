import { Component, Input, OnInit } from '@angular/core';
import {
  AttendanceList,
  AttendanceListDto,
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
  @Input('attendance-list') attendanceListDto: AttendanceListDto[] = [];
  @Input('courseId') courseId!: string;

  attendanceList: AttendanceList[] = [];
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
    this.createIdList(this.attendanceListDto);
  }

  createIdList(attendanceList: AttendanceListDto[]) {
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

        this.attendanceList = this.attendanceListDto.map((attendance) => {
          const student = students.find((s) => s.id === attendance.studentId);
          if (student) {
            return {
              studentId: attendance.studentId,
              firstName: student.firstName,
              lastName: student.lastName,
              present: attendance.present,
            } as AttendanceList;
          }
          return {
            studentId: attendance.studentId,
            firstName: '',
            lastName: '',
            present: attendance.present,
          } as AttendanceList;
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
    const attendanceListDto: AttendanceListDto[] = this.attendanceList.map(
      ({ studentId, present }) => ({
        studentId,
        present,
      })
    );

    this.newLesson.attendanceList = attendanceListDto;

    this.lessonsService.patchLesson(this.lesson.id, this.newLesson).subscribe({
      next: (lesson) => {},
      error: (err) => {
        console.log(err);
      },
      complete: () => {
        this.checked = false;
      },
    });
  }

  openUnEnrollDialog(user: AttendanceList, deleteSound: HTMLAudioElement) {
    deleteSound.play();
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
