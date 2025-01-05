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
  price: string;
  language: string;
  participantsLimit: number;
  participantsNumber: number;
  lessonsLimit: number;
  startDate: string;
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
    public price: string,
    public language: string,
    public participantsLimit: number,
    public participantsNumber: number,
    public lessonsLimit: number,
    public startDate: string,
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
  price: FormControl<string>;
  language: FormControl<string>;
  participantsLimit: FormControl<number>;
  lessonsLimit: FormControl<number>;
  startDate: FormControl<Date | ''>;
  endDate: FormControl<Date | ''>;
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
  startDate: FormControl<Date | ''>;
  endDate: FormControl<Date | ''>;
}

export interface CourseMembers {
  id: number;
  firstName: string;
  lastName: string;
  enrollmentData: Date;
  status: string;
}
