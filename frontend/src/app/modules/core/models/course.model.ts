import { FormControl } from '@angular/forms';

export interface EnrollemntInfo {
  id: number;
  enrollmentData: Date;
  status: string;
}

export type PostCourse = Omit<
  CourseResponse,
  'id' | 'courseStudents' | 'courseTeachers' | 'participantsNumber'
>;

export interface CourseResponse {
  id: string;
  name: string;
  status: string;
  participantsLimit: number;
  participantsNumber: number;
  lessonsNumber: number;
  finishedLessons: number;
  startDate: string;
  endDate: string;
  courseStudents: EnrollemntInfo;
  courseTeachers: EnrollemntInfo;
}

export class Course implements CourseResponse {
  constructor(
    public id: string,
    public name: string,
    public status: string,
    public participantsLimit: number,
    public participantsNumber: number,
    public lessonsNumber: number,
    public finishedLessons: number,
    public startDate: string,
    public endDate: string,
    public courseStudents: EnrollemntInfo,
    public courseTeachers: EnrollemntInfo
  ) {}
}

export interface PostCourseForm {
  name: FormControl<string>;
  status: FormControl<string>;
  participantsLimit: FormControl<number>;
  lessonsNumber: FormControl<number>;
  startDate: FormControl<Date>;
  endDate: FormControl<Date>;
}
