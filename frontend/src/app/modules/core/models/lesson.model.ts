import { FormControl } from '@angular/forms';

export interface AttendanceList {
  studentId: number;
  firstName: string;
  lastName: string;
  present: boolean;
}

export interface Lesson {
  id: string;
  eventName: string;
  startDate: Date;
  endDate: Date;
  teacherId: number;
  courseId: string;
  status: string;
  description: string;
  attendanceList: AttendanceList[];
}

export class Lesson implements Lesson {
  constructor(
    public id: string,
    public eventName: string,
    public startDate: Date,
    public endDate: Date,
    public teacherId: number,
    public courseId: string,
    public status: string,
    public description: string,
    public attendanceList: AttendanceList[]
  ) {}
}

export interface PostLessonForm {
  eventName: FormControl<string>;
  startDate: FormControl<Date>;
  startTime: FormControl<string>;
  endTime: FormControl<string>;
  teacherId: FormControl<number>;
  courseId: FormControl<string>;
  status: FormControl<string>;
  description: FormControl<string>;
}

export type PostLesson = Omit<Lesson, 'id' | 'attendanceList'>;
