import { HttpClient, HttpStatusCode } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment.development';
import { Lesson, PostLesson } from '../models/lesson.model';

@Injectable({
  providedIn: 'root',
})
export class LessonsService {
  apiUrl = environment.apiUrlLessons;
  constructor(private http: HttpClient) {}

  //sprawdzone
  getAllLessons(): Observable<Lesson[]> {
    return this.http.get<Lesson[]>(this.apiUrl);
  }

  getLessonById(id: string): Observable<Lesson> {
    return this.http.get<Lesson>(`${this.apiUrl}/${id}`);
  }

  getLessonsByCourseId(courseId: string): Observable<Lesson[]> {
    return this.http.get<Lesson[]>(`${this.apiUrl}/course-lessons/${courseId}`);
  }

  addLesson(lesson: PostLesson): Observable<Lesson> {
    return this.http.post<Lesson>(this.apiUrl, lesson);
  }

  patchLesson(id: string, lesson: PostLesson): Observable<Lesson> {
    return this.http.patch<Lesson>(`${this.apiUrl}/${id}`, lesson);
  }
  deleteLessonById(id: string): Observable<Record<string, never>> {
    return this.http.delete<Record<string, never>>(`${this.apiUrl}/${id}`);
  }

  enrollStudentLesson(
    lessonId: string,
    studentId: number
  ): Observable<HttpStatusCode> {
    return this.http.post<HttpStatusCode>(
      `${this.apiUrl}/enroll-student-lessons/${lessonId}/${studentId}`,
      []
    );
  }
  // nie sprawdzone

  unEnrollStudentLesson(
    lessonId: string,
    studentId: number
  ): Observable<HttpStatusCode> {
    return this.http.post<HttpStatusCode>(
      `${this.apiUrl}/un-enroll-student-lessons/${lessonId}/${studentId}`,
      []
    );
  }
}
