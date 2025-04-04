import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment.development';
import { PostUser, User } from '../models/user.model';

@Injectable({
  providedIn: 'root',
})
export class StudentService {
  apiUrl = environment.apiUrlStudents;
  constructor(private http: HttpClient) {}

  //ok
  // getStudents(status?: string | null): Observable<User[]> {
  //   return this.http.get<User[]>(`${this.apiUrl}?status=${status}`);
  // }

  getStudents(status?: string | null): Observable<User[]> {
    let url = this.apiUrl;
    if (status) {
      url += `?status=${status}`;
    }
    return this.http.get<User[]>(url);
  }

  getStudentsByIdNumberNotEqual(id: number[]): Observable<User[]> {
    return this.http.post<User[]>(this.apiUrl + '/notIdNumbers', id);
  }

  getStudentsByIdNumbers(id: number[]): Observable<User[]> {
    return this.http.post<User[]>(this.apiUrl + '/idNumbers', id);
  }

  getStudentByEmail(email: string | undefined): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/email?email=${email}`);
  }

  addNewStudent(newStudent: PostUser): Observable<User> {
    return this.http.post<User>(this.apiUrl, newStudent);
  }

  patchStudent(id: number, editedStudent: PostUser): Observable<User> {
    return this.http.patch<User>(`${this.apiUrl}/${id}`, editedStudent);
  }

  deactivateStudentById(id: number): Observable<Record<string, never>> {
    return this.http.delete<Record<string, never>>(
      `${this.apiUrl}/deactivate/${id}`
    );
  }

  deleteStudentById(id: number): Observable<Record<string, never>> {
    return this.http.delete<Record<string, never>>(
      `${this.apiUrl}/remove/${id}`
    );
  }

  getStudentById(id: number): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/${id}`);
  }

  restoreStudentAccount(id: number): Observable<Record<string, never>> {
    return this.http.patch<Record<string, never>>(
      `${this.apiUrl}/restore/${id}`,
      null
    );
  }
}
