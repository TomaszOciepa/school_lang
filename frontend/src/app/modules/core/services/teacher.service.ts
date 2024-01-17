import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { PostTeacher, Teacher } from '../models/teacher.model';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class TeacherService {
  apiUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  getTeacher(): Observable<Teacher[]> {
    return this.http.get<Teacher[]>(this.apiUrl + '/teacher?status=ACTIVE');
  }

  getTeacherById(id: number): Observable<Teacher> {
    return this.http.get<Teacher>(`${this.apiUrl}/teacher/${id}`);
  }

  addNewTeacher(newTeacher: PostTeacher): Observable<Teacher> {
    return this.http.post<Teacher>(`${this.apiUrl}/teacher`, newTeacher);
  }

  deleteTeacherById(id: number): Observable<Record<string, never>> {
    return this.http.delete<Record<string, never>>(
      `${this.apiUrl}/teacher/${id}`
    );
  }

  patchTeacher(id: number, editedTeacher: PostTeacher): Observable<Teacher> {
    return this.http.patch<Teacher>(
      `${this.apiUrl}/teacher/${id}`,
      editedTeacher
    );
  }
}
