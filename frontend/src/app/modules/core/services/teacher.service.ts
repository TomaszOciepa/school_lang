import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { TeacherResponse } from '../models/teacher.model';
import { Observable } from 'rxjs';
import { OAuthService } from 'angular-oauth2-oidc';

@Injectable({
  providedIn: 'root',
})
export class TeacherService {
  constructor(private http: HttpClient, private oAuthService: OAuthService) {}

  getTeacher(): Observable<TeacherResponse[]> {
    return this.http.get<TeacherResponse[]>(
      'http://localhost:8092/teacher/all',
      {
        headers: {
          Authorization: `Bearer ${this.oAuthService.getAccessToken()}`,
        },
      }
    );
  }

  getTeacherById(id: number): Observable<TeacherResponse> {
    return this.http.get<TeacherResponse>(
      `http://localhost:8092/teacher/id/${id}`,
      {
        headers: {
          Authorization: `Bearer ${this.oAuthService.getAccessToken()}`,
        },
      }
    );
  }
}
