import { FormControl } from '@angular/forms';

export interface EnrollemntInfo {
  id: number;
  enrollmentData?: Date;
  status?: string;
}

export type PostCourse = Omit<
  CourseResponse,
  'courseStudents' | 'participantsNumber'
>;

export interface CourseResponse {
  id: string;
  name: string;
  status: string;
  coursePrice: number;
  pricePerLesson: number;
  teacherSharePercentage: number;
  language: string;
  participantsLimit: number;
  participantsNumber: number;
  lessonsLimit: number;
  startDate: Date;
  endDate: string;
  courseStudents: EnrollemntInfo[];
  courseTeachers: EnrollemntInfo[];
  timeRange: string;
  lessonDuration: number;
  teacherId: number;
  courseId: string;
  lessonFrequency: string;
}

export class Course implements CourseResponse {
  constructor(
    public id: string,
    public name: string,
    public status: string,
    public coursePrice: number,
    public pricePerLesson: number,
    public teacherSharePercentage: number,
    public language: string,
    public participantsLimit: number,
    public participantsNumber: number,
    public lessonsLimit: number,
    public startDate: Date,
    public endDate: string,
    public courseStudents: EnrollemntInfo[],
    public courseTeachers: EnrollemntInfo[],
    public timeRange: string,
    public lessonDuration: number,
    public teacherId: number,
    public courseId: string,
    public lessonFrequency: string
  ) {}
}

export interface PostCourseForm {
  name: FormControl<string>;
  lessonsLimit: FormControl<number>;
  pricePerLesson: FormControl<number>;
  teacherSharePercentage: FormControl<number>;
  language: FormControl<string>;
  participantsLimit: FormControl<number>;
  startDate: FormControl<Date>;
  timeRange: FormControl<string>;
  lessonDuration: FormControl<string>;
  teacherId: FormControl<string>;
  lessonFrequency: FormControl<string>;
}

export interface EditCourseForm {
  name: FormControl<string>;
  language: FormControl<string>;
  participantsLimit: FormControl<number>;
  lessonsLimit: FormControl<number>;
}

export interface CourseMembers {
  id: number;
  firstName: string;
  lastName: string;
  enrollmentData: Date;
  status: string;
}
