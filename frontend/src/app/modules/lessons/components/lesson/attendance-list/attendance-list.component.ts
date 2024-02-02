import { Component, Input, OnInit } from '@angular/core';
import {
  AttendanceList,
  Lesson,
} from 'src/app/modules/core/models/lesson.model';
import { StudentService } from 'src/app/modules/core/services/student.service';
import { User } from 'src/app/modules/core/models/user.model';
import { LessonsService } from 'src/app/modules/core/services/lessons.service';

@Component({
  selector: 'app-attendance-list',
  templateUrl: './attendance-list.component.html',
  styleUrls: ['./attendance-list.component.css'],
})
export class AttendanceListComponent implements OnInit {
  @Input('lesson') lesson!: Lesson;
  @Input('attendance-list') attendanceList: AttendanceList[] = [];

  idList: number[] = [];
  students!: User[];
  checked: boolean = false;

  constructor(
    private studentService: StudentService,
    private lessonsService: LessonsService
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
    this.studentService.getStudentsByIdNumber(idList).subscribe({
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
    console.log('lista obecności: ' + JSON.stringify(this.attendanceList));
    this.checked = true;
  }

  save() {
    this.lesson.attendanceList = this.attendanceList;
    this.lessonsService.patchLesson(this.lesson.id, this.lesson).subscribe({
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
}
