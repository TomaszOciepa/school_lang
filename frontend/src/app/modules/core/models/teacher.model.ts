import { FormControl } from '@angular/forms';

export interface TeacherResponse {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  status: boolean;
}

export type PostTeacher = Omit<TeacherResponse, 'id' | 'status'>;

export class Teacher implements TeacherResponse {
  constructor(
    public id: number,
    public firstName: string,
    public lastName: string,
    public email: string,
    public status: boolean
  ) {}
}

export interface PostTeacherForm {
  firstName: FormControl<string>;
  lastName: FormControl<string>;
  email: FormControl<string>;
}
