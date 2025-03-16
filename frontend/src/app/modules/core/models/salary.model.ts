export interface SalaryResponse {
  id: string;
  date: Date;
  teacherId?: number;
  payoutAmount?: number;
  courseId: string;
  status: string;
  lessons: LessonResponse[];
}

export class Salary implements SalaryResponse {
  constructor(
    public id: string,
    public date: Date,
    public teacherId: number,
    public payoutAmount: number,
    public courseId: string,
    public status: string,
    public lessons: LessonResponse[]
  ) {}
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
  language: string;
  price: number;
}
