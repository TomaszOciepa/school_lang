import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment.development';
import { Salary } from '../models/salary.model';

@Injectable({
  providedIn: 'root',
})
export class SalaryService {
  apiUrl = environment.apiUrlSalary;

  constructor(private http: HttpClient) {}

  getSalary(id: number): Observable<Salary[]> {
    return this.http.get<Salary[]>(this.apiUrl + '/' + id);
  }

  getSalaryById(id: string): Observable<Salary> {
    return this.http.get<Salary>(this.apiUrl + '/details/' + id);
  }
}
