import { HttpClient, HttpStatusCode } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment.development';
import { Course, CourseMembers, PostCourse } from '../models/course.model';
import { User } from '../models/user.model';

@Injectable({
  providedIn: 'root',
})
export class CourseService {
  apiUrl = environment.apiUrlCourses;
  constructor(private http: HttpClient) {}

  // ok
  addCourse(course: PostCourse): Observable<Course> {
    return this.http.post<Course>(this.apiUrl, course);
  }

  getAllByStatus(): Observable<Course[]> {
    return this.http.get<Course[]>(this.apiUrl);
  }

  getCoursesByLanguage(language: string): Observable<Course[]> {
    return this.http.get<Course[]>(
      `${this.apiUrl}/course-offering/${language}`
    );
  }

  getCourseById(id: string): Observable<Course> {
    return this.http.get<Course>(`${this.apiUrl}/${id}`);
  }
  getCourseMembers(courseId: string): Observable<CourseMembers[]> {
    return this.http.get<CourseMembers[]>(`${this.apiUrl}/members/${courseId}`);
  }

  getCourseTeachers(courseId: string): Observable<User[]> {
    return this.http.get<User[]>(`${this.apiUrl}/teacher/${courseId}`);
  }

  patchCourse(id: string, editedCourse: PostCourse): Observable<Course> {
    return this.http.patch<Course>(`${this.apiUrl}/${id}`, editedCourse);
  }

  deleteCourseById(id: string): Observable<Record<string, never>> {
    return this.http.delete<Record<string, never>>(`${this.apiUrl}/${id}`);
  }

  assignTeacherToCourse(
    courseId: string,
    teacherId: number
  ): Observable<HttpStatusCode> {
    return this.http.post<HttpStatusCode>(
      `${this.apiUrl}/${courseId}/teacher/${teacherId}`,
      []
    );
  }

  teacherCourseUnEnrollment(
    courseId: string,
    teacherId: number
  ): Observable<HttpStatusCode> {
    return this.http.delete<HttpStatusCode>(
      `${this.apiUrl}/${courseId}/teacher/${teacherId}`
    );
  }

  assignStudentToCourse(
    courseId: string,
    studentId: number
  ): Observable<HttpStatusCode> {
    return this.http.post<HttpStatusCode>(
      `${this.apiUrl}/${courseId}/student/${studentId}`,
      []
    );
  }

  studentCourseUnEnrollment(
    courseId: string,
    studentId: number
  ): Observable<HttpStatusCode> {
    return this.http.delete<HttpStatusCode>(
      `${this.apiUrl}/${courseId}/student/${studentId}`
    );
  }

  restoreStudentToCourse(
    courseId: string,
    studentId: number
  ): Observable<HttpStatusCode> {
    return this.http.post<HttpStatusCode>(
      `${this.apiUrl}/restore/${courseId}/student/${studentId}`,
      []
    );
  }

  getCourseByTeacherId(teacherId: number): Observable<Course[]> {
    return this.http.get<Course[]>(
      `${this.apiUrl}/teacher-courses/${teacherId}`
    );
  }

  getCourseByStudentId(studentId: number): Observable<Course[]> {
    return this.http.get<Course[]>(
      `${this.apiUrl}/student-courses/${studentId}`
    );
  }

  isStudentEnrolledInCourse(
    course: Course,
    studentId: number
  ): Observable<boolean> {
    return this.http.post<boolean>(
      `${this.apiUrl}/is-student/${studentId}/enroll-course`,
      course
    );
  }
}
