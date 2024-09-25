import { HttpClient, HttpStatusCode } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment.development';
import { Order } from '../models/order.model';

@Injectable({
  providedIn: 'root',
})
export class OrderService {
  apiUrl = environment.apiUrlOrder;

  constructor(private http: HttpClient) {}

  getAllOrders(): Observable<Order[]> {
    return this.http.get<Order[]>(this.apiUrl);
  }

  getById(id: string): Observable<Order> {
    return this.http.get<Order>(`${this.apiUrl}/${id}`);
  }

  getOrdersByStudentId(id: number): Observable<Order[]> {
    return this.http.get<Order[]>(`${this.apiUrl}/student/${id}`);
  }

  getOrderByOrderNumber(number: string): Observable<Order> {
    return this.http.get<Order>(`${this.apiUrl}/number/${number}`);
  }

  createOrder(courseId: String, studentId: number): Observable<HttpStatusCode> {
    return this.http.post<HttpStatusCode>(
      `${this.apiUrl}/course/${courseId}/student/${studentId}`,
      []
    );
  }

  deleteOrderById(id: string): Observable<Record<string, never>> {
    return this.http.delete<Record<string, never>>(
      `${this.apiUrl}/delete/${id}`
    );
  }
}
