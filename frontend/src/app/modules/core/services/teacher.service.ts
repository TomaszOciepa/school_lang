import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { TeacherResponse } from '../models/teacher.model';
import { Observable } from 'rxjs';
import { OAuthService } from 'angular-oauth2-oidc';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class TeacherService {
  apiUrl = environment.apiUrl;

  constructor(private http: HttpClient, private oAuthService: OAuthService) {}

  getTeacher(): Observable<TeacherResponse[]> {
    return this.http.get<TeacherResponse[]>(this.apiUrl + '/teacher/all', {
      headers: {
        Authorization: `Bearer ${this.oAuthService.getAccessToken()}`,
      },
    });
  }

  getTeacherById(id: number): Observable<TeacherResponse> {
    return this.http.get<TeacherResponse>(`${this.apiUrl}/teacher/id/${id}`, {
      headers: {
        Authorization: `Bearer ${this.oAuthService.getAccessToken()}`,
      },
    });
  }
}
