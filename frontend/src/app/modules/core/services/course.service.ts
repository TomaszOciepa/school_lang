import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment.development';
import { Course, PostCourse } from '../models/course.model';

@Injectable({
  providedIn: 'root',
})
export class CourseService {
  apiUrl = environment.apiUrlCourses;
  constructor(private http: HttpClient) {}

  getCourses(): Observable<Course[]> {
    return this.http.get<Course[]>(this.apiUrl + '?status=ACTIVE');
  }

  getCourseById(id: string): Observable<Course> {
    return this.http.get<Course>(`${this.apiUrl}/${id}`);
  }

  addNewCourse(newCourse: PostCourse): Observable<Course> {
    return this.http.post<Course>(this.apiUrl, newCourse);
  }

  deleteCourseById(id: string): Observable<Record<string, never>> {
    return this.http.delete<Record<string, never>>(`${this.apiUrl}/${id}`);
  }

  patchCourse(id: string, editedCourse: PostCourse): Observable<Course> {
    return this.http.patch<Course>(`${this.apiUrl}/${id}`, editedCourse);
  }
}
