import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment.development';
import { PostUser, User } from '../models/user.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class RegisterService {
  apiUrl = environment.registerService;

  constructor(private http: HttpClient) {}

  createAccountStudent(newUser: PostUser): Observable<User> {
    return this.http.post<User>(this.apiUrl + '/student', newUser);
  }

  createAccountTeacher(newUser: PostUser): Observable<User> {
    return this.http.post<User>(this.apiUrl + '/teacher', newUser);
  }
}
