import { FormControl } from '@angular/forms';

export interface AttendanceList {
  studentId: number;
  firstName: string;
  lastName: string;
  present: boolean;
}

export interface LessonResponse {
  id: string;
  eventName: string;
  startDate: string;
  endDate: string;
  teacherId: number;
  courseId: string;
  status: string;
  description: string;
  attendanceList: AttendanceList[];
}

export interface GenerateLessonsResponse {
  timeRange: string;
  lessonDuration: number;
  teacherId: number;
  courseId: string;
  lessonFrequency: string;
}

export class GenerateLessons implements GenerateLessonsResponse {
  constructor(
    public timeRange: string,
    public lessonDuration: number,
    public teacherId: number,
    public courseId: string,
    public lessonFrequency: string
  ) {}
}

export class Lesson implements LessonResponse {
  constructor(
    public id: string,
    public eventName: string,
    public startDate: string,
    public endDate: string,
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
  teacherId: FormControl<string>;
  // status: FormControl<string>;
  description: FormControl<string>;
}

export interface LessonsGeneratedForm {
  timeRange: FormControl<string>;
  lessonDuration: FormControl<string>;
  teacherId: FormControl<string>;
  lessonFrequency: FormControl<string>;
}

export type PostLesson = Omit<LessonResponse, 'id' | 'attendanceList'>;
