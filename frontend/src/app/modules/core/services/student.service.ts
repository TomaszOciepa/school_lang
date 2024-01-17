import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment.development';
import { PostUser, User } from '../models/teacher.model';

@Injectable({
  providedIn: 'root',
})
export class StudentService {
  apiUrl = environment.apiUrlStudents;
  constructor(private http: HttpClient) {}

  getTeacher(): Observable<User[]> {
    return this.http.get<User[]>(this.apiUrl + '/teacher?status=ACTIVE');
  }

  getTeacherById(id: number): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/teacher/${id}`);
  }

  addNewTeacher(newTeacher: PostUser): Observable<User> {
    return this.http.post<User>(`${this.apiUrl}/teacher`, newTeacher);
  }

  deleteTeacherById(id: number): Observable<Record<string, never>> {
    return this.http.delete<Record<string, never>>(
      `${this.apiUrl}/teacher/${id}`
    );
  }

  patchTeacher(id: number, editedTeacher: PostUser): Observable<User> {
    return this.http.patch<User>(`${this.apiUrl}/teacher/${id}`, editedTeacher);
  }
}
