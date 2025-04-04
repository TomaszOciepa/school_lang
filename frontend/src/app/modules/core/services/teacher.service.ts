import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { PostUser, User } from '../models/user.model';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment.development';

@Injectable({
  providedIn: 'root',
})
export class TeacherService {
  apiUrl = environment.apiUrlTeacher;

  constructor(private http: HttpClient) {}

  // ok

  // getTeachers(status?: string | null): Observable<User[]> {
  //   return this.http.get<User[]>(`${this.apiUrl}?status=${status}`);
  // }

  getTeachers(status?: string | null): Observable<User[]> {
    let url = this.apiUrl;
    if (status) {
      url += `?status=${status}`;
    }
    return this.http.get<User[]>(url);
  }

  getTeacherById(id: number): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/${id}`);
  }

  getTeacherByEmail(email: string | undefined): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/email?email=${email}`);
  }

  getTeachersByIdNumberNotEqual(id: number[]): Observable<User[]> {
    return this.http.post<User[]>(this.apiUrl + '/id-numbers-not-equal', id);
  }

  addNewTeacher(newTeacher: PostUser): Observable<User> {
    return this.http.post<User>(this.apiUrl, newTeacher);
  }

  deactivateTeacherById(id: number): Observable<Record<string, never>> {
    return this.http.delete<Record<string, never>>(`${this.apiUrl}/${id}`);
  }

  deleteTeacherById(id: number): Observable<Record<string, never>> {
    return this.http.delete<Record<string, never>>(
      `${this.apiUrl}/remove/${id}`
    );
  }

  patchTeacher(id: number, editedTeacher: PostUser): Observable<User> {
    return this.http.patch<User>(`${this.apiUrl}/${id}`, editedTeacher);
  }

  restoreTeacherAccount(id: number): Observable<Record<string, never>> {
    return this.http.patch<Record<string, never>>(
      `${this.apiUrl}/restore/${id}`,
      null
    );
  }
}
