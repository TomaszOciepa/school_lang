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

  getStudent(): Observable<User[]> {
    return this.http.get<User[]>(this.apiUrl + '?status=ACTIVE');
  }

  getStudentById(id: number): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/${id}`);
  }

  addNewStudent(newStudent: PostUser): Observable<User> {
    return this.http.post<User>(this.apiUrl, newStudent);
  }

  deleteStudentById(id: number): Observable<Record<string, never>> {
    return this.http.delete<Record<string, never>>(`${this.apiUrl}/${id}`);
  }

  patchStudent(id: number, editedStudent: PostUser): Observable<User> {
    return this.http.patch<User>(`${this.apiUrl}/${id}`, editedStudent);
  }

  getStudentsByNotIdNumber(id: number[]): Observable<User[]> {
    return this.http.post<User[]>(this.apiUrl + '/notIdNumbers', id);
  }

  getStudentsByIdNumber(id: number[]): Observable<User[]> {
    return this.http.post<User[]>(this.apiUrl + '/idNumbers', id);
  }
}
