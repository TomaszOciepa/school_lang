export interface AttendanceList {
  studentId: number;
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
  attendanceList: AttendanceList;
}
