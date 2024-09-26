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
    public courseTeachers: EnrollemntInfo[]
  ) {}
}

export interface PostCourseForm {
  name: FormControl<string>;
  // status: FormControl<string>;
  price: FormControl<string>;
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
