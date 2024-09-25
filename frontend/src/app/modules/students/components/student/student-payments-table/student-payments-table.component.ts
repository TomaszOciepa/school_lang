import { Component, ErrorHandler, Input, ViewChild } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Order } from 'src/app/modules/core/models/order.model';
import { OrderService } from 'src/app/modules/core/services/order.service';

@Component({
  selector: 'app-student-payments-table',
  templateUrl: './student-payments-table.component.html',
  styleUrls: ['./student-payments-table.component.css'],
})
export class StudentPaymentsTableComponent {
  @Input('student-id') studentId!: number;

  displayedColumns: string[] = [
    'lp',
    'createDate',
    'courseId',
    'orderNumber',
    'status',
    'totalAmount',
    'paymentServiceNumber',
  ];
  dataSource!: MatTableDataSource<Order>;

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(private orderService: OrderService, private dialog: MatDialog) {}

  async ngOnInit(): Promise<void> {
    this.getStudentOrders();
  }

  private getStudentOrders() {
    this.orderService.getOrdersByStudentId(this.studentId).subscribe({
      next: (orders) => {
        this.dataSource = new MatTableDataSource<Order>(orders);
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
      },
      error: (err: ErrorHandler) => {
        console.log(err);
      },
    });
  }

  applyFilter(event: Event) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.dataSource.filter = filterValue.trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }
  }
}
