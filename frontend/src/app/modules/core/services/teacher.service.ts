import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { PostTeacher, Teacher } from '../models/teacher.model';
import { Observable } from 'rxjs';
import { OAuthService } from 'angular-oauth2-oidc';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class TeacherService {
  apiUrl = environment.apiUrl;

  constructor(private http: HttpClient, private oAuthService: OAuthService) {}

  getTeacher(): Observable<Teacher[]> {
    return this.http.get<Teacher[]>(
      this.apiUrl + '/teacher/all?status=ACTIVE',
      {
        headers: {
          Authorization: `Bearer ${this.oAuthService.getAccessToken()}`,
        },
      }
    );
  }

  getTeacherById(id: number): Observable<Teacher> {
    return this.http.get<Teacher>(`${this.apiUrl}/teacher/id/${id}`, {
      headers: {
        Authorization: `Bearer ${this.oAuthService.getAccessToken()}`,
      },
    });
  }

  addNewTeacher(newTeacher: PostTeacher): Observable<Teacher> {
    return this.http.post<Teacher>(`${this.apiUrl}/teacher`, newTeacher, {
      headers: {
        Authorization: `Bearer ${this.oAuthService.getAccessToken()}`,
      },
    });
  }

  deleteTeacherById(id: number): Observable<Record<string, never>> {
    return this.http.delete<Record<string, never>>(
      `${this.apiUrl}/teacher/${id}`,
      {
        headers: {
          Authorization: `Bearer ${this.oAuthService.getAccessToken()}`,
        },
      }
    );
  }

  patchTeacher(id: number, editedTeacher: PostTeacher): Observable<Teacher> {
    return this.http.patch<Teacher>(
      `${this.apiUrl}/teacher/${id}`,
      editedTeacher,
      {
        headers: {
          Authorization: `Bearer ${this.oAuthService.getAccessToken()}`,
        },
      }
    );
  }
}
