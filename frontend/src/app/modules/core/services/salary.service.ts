import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment.development';

@Injectable({
  providedIn: 'root',
})
export class SalaryService {
  apiUrl = environment.apiUrlSalary;

  constructor(private http: HttpClient) {}

  createSalary(id: number): Observable<any> {
    return this.http.get(this.apiUrl + '/create-salary/' + id);
  }
}
